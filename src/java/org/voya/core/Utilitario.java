package org.voya.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Lourival Almeida
 */
public class Utilitario {

    private static DecimalFormatSymbols dfs;
    private static NumberFormat formatterHora;
    private static final String hexDigits = "0123456789abcdef";
    private static Matcher matcher;
    private static Pattern pattern;
 
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
    static {
        dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        formatterHora = new DecimalFormat("###,###,###,###.##", dfs);
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public static Date parse(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return sdf.parse(data);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date parseDataHora(String data)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        return sdf.parse(data);
    }

    public static Date parseHora(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return sdf.parse(data);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String format(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return sdf.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatoDataReduzida(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return sdf.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatarDiaMesAbreviado(Date dataInicio, Date dataFim) {
        StringBuilder sb = new StringBuilder();

        sb.append(formatoDataReduzida(dataInicio));

        if (dataFim != null) {
            sb.append(" a ");
            sb.append(formatoDataReduzida(dataFim));
        }
        return sb.toString();
    }

    public static String formatarDataParaPesquisa(Date data1, Date data2) {
        StringBuilder sb = new StringBuilder();
        if (data2 != null) {
            Calendar calData1 = Calendar.getInstance();
            calData1.setTime(data1);
            Calendar calData2 = Calendar.getInstance();
            calData2.setTime(data2);

            while (calData1.compareTo(calData2) < 1) {
                sb.append(formatarDataParaPesquisa(calData1.getTime()));
                sb.append(" ");
                calData1.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            return formatarDataParaPesquisa(data1);
        }
        return sb.toString();
    }

    public static String formatarDataParaPesquisa(Date data) {
        if (data == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyy");
        return sdf.format(data) + " " + sdf2.format(data);
    }

    /**
     * retorna o mes no formato OUTUBRO por exemplo *
     */
    public static String retornaMesCompleto(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM", new Locale("pt", "BR"));
            return sdf.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String retornaMesExtenso(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", new Locale("pt", "BR"));
            return sdf.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String retornaAno(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            return sdf.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formata o dia, mes e ano desta forma: 24.Nov.09
     *
     * @param data
     * @return
     */
    public static String formatarDiaMesAnoAbreviado(Date dataInicio, Date dataFim) {
        StringBuilder sb = new StringBuilder();

        sb.append(format(dataInicio));

        if (dataFim != null) {
            sb.append(" a ");
            sb.append(format(dataFim));
        }
        return sb.toString();
    }

    public static String formatDataHora(Date data)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        return sdf.format(data);
    }

    public static String format(Date data, String formato)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        return sdf.format(data);
    }

    /**
     * Retorna uma data com timezone correto
     */
    public static Date getDateTimeZone() {
        //return Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo")).getTime();
        return new Date();
    }

    public static String formatarHora(Date data)
            throws Exception {
        return format(data, "HH:mm:ss");
    }

    public static String formatarHoraMinuto(Date data)
            throws Exception {
        String hora;
        if (data != null) {
            hora = "(" + format(data, "HH:mm") + ")";
        } else {
            hora = "";
        }
        return hora;
    }

    /**
     * Transforma um valor em horas para milisegundos
     */
    public static long transformarHoraMili(long horas) {
        //return horas * 60 * 60 * 1000;
        return horas * 3600000;
    }

    /**
     * Transforma um valor em milisegundos para horas Obs.: Esta operacao
     * perdera precisao dos valores.
     */
    public static BigDecimal transformarMiliHora(long mili) {
        BigDecimal miliSeconds = new BigDecimal(mili);
        BigDecimal taxa = new BigDecimal(3600000);
        BigDecimal res = miliSeconds.divide(taxa, BigDecimal.ROUND_DOWN);

        //return mili / 60 / 60 / 1000;
        return res;
    }

    /*
     * Retira os pontos e vírgulas do número. Método serve para utilização no
     * PagSeguro.
     */
    public static String formatarValorMonetario(BigDecimal valor) {
        String aux = valor.toString();
        int i = aux.indexOf(".");
        if (i >= 0) {
            aux = aux.substring(0, i + 3);
            aux = aux.replace(".", "");
        } else {
            aux = aux + "00";
        }

        return aux;
    }

    /*
     * public static void main (String str[]) {
     * System.out.println(Utilitario.formatarValorMonetario(new
     * BigDecimal("123.4567")));
    }
     */
    /**
     * Formata o valor como uma hora
     */
    public static String formatarDouble(BigDecimal valor) {
        return formatterHora.format(valor.doubleValue());
    }

    public static String formataHoraMinutoSegundo(long mili) {
        int nHoras = (int) (mili / 3600000);
        int nHorasResto = (int) (mili % 3600000);

        StringBuilder sb = new StringBuilder();

        sb.append(String.valueOf(nHoras));
        sb.append("hs : ");

        nHoras = (int) (nHorasResto / 60000);
        nHorasResto = (int) (nHorasResto % 60000);

        sb.append(String.valueOf(nHoras));
        sb.append("min : ");

        nHoras = (int) (nHorasResto / 1000);

        sb.append(String.valueOf(nHoras));
        sb.append("s");

        return sb.toString();
    }

    public static boolean popular(Object target, Object source) {
        try {
            BeanUtils.populate(target, BeanUtils.describe(source));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private static String stringHexa(byte[] b) {

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            int j = ((int) b[i]) & 0xFF;
            buf.append(hexDigits.charAt(j / 16));
            buf.append(hexDigits.charAt(j % 16));
        }

        return buf.toString();
    }

    private static byte[] gerarHash(String frase) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String encriptar(String valor) {
        return stringHexa(gerarHash(valor));
    }

    /**
     * Corta uma string na última palavra antes do tamanho de caracteres ser
     * alcançado.
     *
     * @param tamanho
     * @return
     */
    public static String cortarString(String str, int tamanho) {
        if (str.length() > tamanho) {
            String aux = str.substring(0, tamanho);
            return aux.substring(0, aux.lastIndexOf(" ")) + "...";
        } else {
            return str;
        }
    }

    /*
     * Trunca uma string no tamanho informado
     */
    public static String truncarString(String str, int tamanho) {
        if (str.length() > tamanho) {
            return str.substring(0, tamanho - 1);
        } else {
            return str;
        }
    }

    public static String separarLinhas(String origem) {
        if (origem == null) {
            return "";
        }
        String aux = origem.replaceAll("\n", "<br/>");
        return aux.replaceAll("\r\n", "<br/>");
    }

    /**
     * Retorna a extensão de um arquivo a partir do seu tipo
     *
     * @param contentType
     * @return
     */
    public static String getExtensaoArquivo(String fileName) {
        String s[] = fileName.split("\\.");
        return "." + s[s.length - 1];
    }

    public static String limparCPF(String cpf) {
        String aux = cpf.replace(".", "");
        return aux.replace("-", "");
    }

    /**
     * Popular um bean a partir de um mapa de informações passado como parâmetro
     * e a partir de um filtro de campos que deve ser realizado. Se o campo for
     * "" não será setado.
     *
     * @param origem
     * @param bean
     * @param filtro
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void popular(Map origem, Object bean, String filtro[])
            throws IllegalAccessException, InvocationTargetException {
        Map mapaFiltrado = new HashMap();

        for (String x : filtro) {
            if (!"".equals(origem.get(x))) {
                mapaFiltrado.put(x, origem.get(x));
            }
        }
        BeanUtils.populate(bean, mapaFiltrado);
    }

    /**
     * Retira a parte da URL do evento que diz respeito ao usuário
     *
     * @param str
     * @return
     */
    public static String limparURL(String str) {
        if (str == null) {
            return null;
        }

        StringTokenizer st = new StringTokenizer(str, "/");
        if (st.countTokens() == 2) {
            st.nextToken();
            return st.nextToken();
        }
        return str;
    }

    public static String formatGMT(Date data)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(data);
    }

    public static String antiXSSString(String original) {
        return AntiXSS.EncodeHtmlSimples(original);
    }

    /*
     * faz um split de uma string separada por @ Deve vir valores do tipo:
     * "bookmark.html@1_35345345.html" O primeiro nome é um título, o segundo o
     * caminho físico
     */
    public static String split(String valor, int i) {
        if (valor == null || valor.equals(""))
            return "";
        
        if (i == 1) //pegar primeiro parametro
        {
            return valor.substring(0, valor.indexOf("@"));
        } else {
            return valor.substring(valor.indexOf('@') + 1);
        }
    }

    public static String getURLFromMsg(String text) 
    {
        ArrayList links = new ArrayList();

        String regex = "\\(?\\b(http[s]*://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }
        
        if (links.size() > 0)
            return (String)links.get(0);
        else
            return null;
    }
    
    /*
     * Compara duas datas e retorna um label a ser utilizado na aplicação
     * para indicar que um item é novidadae
     */
    public static String cData(Date ultimoAcesso, Date data)
    {
        if (ultimoAcesso == null || data == null)
            return null;
        
        if (ultimoAcesso.before(data))
            return "<span class='label label-important'>novo</span>";
        //Verificando se é novo por dois dias.
        else if (Math.abs(ultimoAcesso.getTime() - data.getTime()) < 172800000)
            return "<span class='label label-important'>novo</span>";
        else
            return null;
    }
    
    public static boolean ehEmail(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}