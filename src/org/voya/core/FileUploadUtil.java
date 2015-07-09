package org.voya.core;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadUtil 
{
    public static ServletFileUpload getUploadHandler()
    {
        return VoyaServlet.upload;
    }
    
    public static HashMap processarParametros(HttpServletRequest request) 
        throws FileUploadException, Exception
    {
        HashMap res = new HashMap();
        List<FileItem> items = VoyaServlet.upload.parseRequest(request);
        
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (item.isFormField())
                res.put(item.getFieldName(), item.getString());
            else
                res.put(item.getFieldName(), item);
        }
        return res;
    }
    
    public static void processarUpload(HttpServletRequest request, String fileName, long limitSizeInBytes) 
        throws FileUploadException, Exception
    {
        List<FileItem> items = VoyaServlet.upload.parseRequest(request);
        
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (!item.isFormField()) {
                //String contentType = item.getContentType();
                
                if (limitSizeInBytes >= item.getSize())
                {
                    File uploadedFile = new File(fileName);
                    item.write(uploadedFile);
                }
            }
        }
    }
    
    private static String  parseName(String s, StringBuffer sb) 
    {
	sb.setLength(0);

	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i); 
	    switch (c) {
	    case '+':
		sb.append(' ');
		break;

	    case '%':
		try {
		    sb.append((char) Integer.parseInt(s.substring(i+1, i+3), 16));
		    i += 2;
		} catch (NumberFormatException e) {
		    throw new IllegalArgumentException();
		} catch (StringIndexOutOfBoundsException e) {
		    String rest  = s.substring(i);
		    sb.append(rest);
		    if (rest.length()==2)
			i++;
		}
		break;

	    default:
		sb.append(c);
		break;
	    }
	}
	return sb.toString();
    }
    
    
    public static HashMap parseQueryString(String s) 
    {
	String valArray[] = null;
	if (s == null) {
	    throw new IllegalArgumentException();
	}

	HashMap ht = new HashMap();
	StringBuffer sb = new StringBuffer();
	StringTokenizer st = new StringTokenizer(s, "&");

	while (st.hasMoreTokens()) {
	    String pair = (String)st.nextToken();
	    int pos = pair.indexOf('=');
	    if (pos == -1) {
		throw new IllegalArgumentException();
	    }

	    String key = parseName(pair.substring(0, pos), sb);
	    String val = parseName(pair.substring(pos+1, pair.length()), sb);

	    if (ht.containsKey(key)) {
		String oldVals[] = (String []) ht.get(key);
		valArray = new String[oldVals.length + 1];
		for (int i = 0; i < oldVals.length; i++) 
		    valArray[i] = oldVals[i];

		valArray[oldVals.length] = val;

	    } else {
		valArray = new String[1];
		valArray[0] = val;
	    }
	    ht.put(key, valArray);
	}
	return ht;
    }
    
}
