
package test;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.PrintWriter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author roddycorrea
 */
public class test {
    private static String service = "http://localhost:8890/sparql";
    private static String defaultGraph = "http://localhost:8890/ejemplo";
    public static void queryClass(){
        
        String sparql = "Select ?s where {?s ?p ?o}";
        Query query = QueryFactory.create(sparql);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        
        try {
            ResultSet results = qexec.execSelect();
//            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
//            pw = response.getWriter();
            
        while(results.hasNext()){
            System.out.println(results.next());
//            QuerySolution rs = results.nextSolution();
//            RDFNode s = rs.get("s");
//            RDFNode p = rs.get("p");
//            RDFNode o = rs.get("o");
//            pw.write("<table>");
//                pw.write("<tr>");
//                    pw.write("<td><b>Sujeto:</b></td>");
//                    pw.write("<td>"+s+"</td>");
//                pw.write("</tr>");
//                pw.write("<tr>");
//                    pw.write("<td><b>Predicado:</b></td>");
//                    pw.write("<td>"+p+"</td>");
//                pw.write("</tr>");
//                pw.write("<tr>");
//                    pw.write("<td><b>Objeto:</b></td>");
//                    pw.write("<td>"+o+"</td>");
//                pw.write("</tr><br />");
//            pw.write("</table>");
//            System.out.println(s + " " + p + " " + o);

        }
        } catch (Exception e) {
            System.out.println("La consulta no ha podido realizarse " + e);
        } finally {
            qexec.close();
        }
    }
    public static void main(String[] args) {
        queryClass();
    }
}
