
package beans;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverAction;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import util.util;

/**
 *
 * @author roddycorrea
 */
@ManagedBean
@RequestScoped
public class BeanFunctions {

    private static String service = "http://localhost:8890/sparql";
    private static String defaultGraph = "http://localhost:8890/censo";
    public BeanFunctions() {
    }
    
    public static String resultConnect() {
        util conn = new util();
        Connection cn = conn.getConnect();
        String result;
        if (cn == null) {
            result = "Problemas en la conexión";
        }
        else{
            result= "Conexion realizada correctamente";
        }
        return result;
    }
    
    public void queryClass(){
//        String sparql = "prefix animales:<http://localhost:8890/ejemplo>\n"+
//                        "select ?s ?o ?p\n"+
//                        "where{\n"+
//                        "?s ?p ?o."+
//                        "filter (?s=<animales:Vertebrados>)"+
//                        "}";
//        String sparql = "prefix Flora:<http://localhost:8890/lily>\n"+
//                        "select *\n"+
//                        "where{\n"+
//                        "?s ?p ?o.\n"+
//                        "filter (?s=<Flora:Reproduccion>)\n"+
//                        "}\n";
        //System.out.println(sparql);
        String sparql = "Select * where {?s ?p ?o}";
        Query query = QueryFactory.create(sparql);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                RDFNode p = rs.get("p");
                RDFNode o = rs.get("o");
                pw.write("<table>");
                    pw.write("<tr>");
                        pw.write("<td><b>Sujeto:</b></td>");
                        pw.write("<td>"+s+"</td>");
                    pw.write("</tr>");
                    pw.write("<tr>");
                        pw.write("<td><b>Predicado:</b></td>");
                        pw.write("<td>"+p+"</td>");
                    pw.write("</tr>");
                    pw.write("<tr>");
                        pw.write("<td><b>Objeto:</b></td>");
                        pw.write("<td>"+o+"</td>");
                    pw.write("</tr><br />");
                pw.write("</table>");
                
            }
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    
}
