package org.voya.core;

import java.io.File;
import java.util.Iterator;
import java.util.List;
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
}
