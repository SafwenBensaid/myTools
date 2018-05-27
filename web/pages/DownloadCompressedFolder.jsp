<%@page import="dto.VerificationDeltaChampsDTO"%>
<%@ page  import="java.io.FileInputStream" %>
<%@ page  import="java.io.BufferedInputStream"  %>
<%@ page  import="java.io.File"  %>
<%@ page import="java.io.IOException" %>


<%

    // you  can get your base and parent from the database

    VerificationDeltaChampsDTO champs = (VerificationDeltaChampsDTO) request.getSession().getAttribute("VerificationDeltaChamps");
    String filepath = champs.getUrlCompressedFolder();
    System.out.println("download: "+filepath);
    BufferedInputStream buf = null;
    ServletOutputStream myOut = null;

    try {

        myOut = response.getOutputStream();
        File myfile = new File(filepath);
        String filename = champs.getNameCompressedFolder();

        //set response headers
        response.setContentType("text/plain");

        response.addHeader(
                "Content-Disposition", "attachment; filename=" + filename);

        response.setContentLength((int) myfile.length());

        FileInputStream input = new FileInputStream(myfile);
        buf = new BufferedInputStream(input);
        int readBytes = 0;

        //read from the file; write to the ServletOutputStream
        while ((readBytes = buf.read()) != -1) {
            myOut.write(readBytes);
        }

    } catch (IOException ioe) {

        throw new ServletException(ioe.getMessage());

    } finally {

        //close the input/output streams
        if (myOut != null) {
            myOut.close();
        }
        if (buf != null) {
            buf.close();
        }

    }



%>
