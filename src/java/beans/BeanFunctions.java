
package beans;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.IOException;
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
    private static String defaultGraph = "http://localhost:8890/poblacion";
    private static String queryAll = "select * where  {?s ?p ?o}";
    private static String queryClass = "select ?s where  {?s ?p <http://www.w3.org/2002/07/owl#Class>}";
    private static String queryProperties = "select ?s where  {?s ?p <http://www.w3.org/2002/07/owl#DatatypeProperty>}";
    private static String queryPropertiesRangeDomain = "select ?s ?range ?domain where  {?s <http://www.w3.org/2000/01/rdf-schema#domain> ?domain. ?s <http://www.w3.org/2000/01/rdf-schema#range> ?range. }";
    private static String queryClassSubClass = "select ?subClassOf ?s where {?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?subClassOf.}";
    private static String queryPerson = "prefix foaf:<http://xmlns.com/foaf/0.1/>\n"+
                                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <foaf:Person>.}";
    private static String queryProvince = "prefix foaf:<http://purl.org/ontology/places/>\n" +
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <place:Province>.}";
    private static String queryCountry = "prefix place:<http://purl.org/ontology/places/>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <place:Country>.}";
    private static String queryCanton = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:Canton>.}";
    private static String queryCenso = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:Censo>.}";
    private static String queryDatosPersonales = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:DatosPersonales>.}";
    private static String queryDiscapacidad = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:Discapacidad>.}";
    private static String queryFamilia = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:Familia>.}";
    private static String queryMiembros = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:Miembros>.}";
    private static String queryNivelDeInstruccion = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:NiveldeInstruccion>.}";
    private static String queryPoblacion = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:Poblacion>.}";
    private static String queryUsaTecnology = "prefix pobl:<htttp://semanticweb.ec/ontology/poblacion.owl>\n"+
                        "select ?s where { ?s <http://www.w3.org/2000/01/rdf-schema#domain> <pobl:UsaTecnology>.}";
    
    private static String queryClassProperties = "prefix Flora:<http://localhost:8890/lily>\n"+
                        "select *\n"+
                        "where{\n"+
                        "?s ?p ?o.\n"+
                        "filter (?s=<Flora:Reproduccion>)\n"+
                        "}\n";
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
            result= "Conexión realizada correctamente";
        }
        return result;
    }
    
    public void queryClass(){
        Query query = QueryFactory.create(queryClass);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        
        PrintWriter pw = null;
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            while(results.hasNext()){
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");

                    pw.write(s+" ║ ");
            }
        } catch (IOException e) {
            res = "La consulta no ha podido realizarse por " + e;
        } finally {
            qexec.close();
        }
    }
    
    public void queryProperties(){
        Query query = QueryFactory.create(queryProperties);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        
        PrintWriter pw = null;
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            while(results.hasNext()){
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                    pw.write(s+" ║ ");
            }
        } catch (IOException e) {
            res = "La consulta no ha podido realizarse por " + e;
        } finally {
            qexec.close();
        }
    }
    
    public void queryPropertiesRangeDomain(){
        Query query = QueryFactory.create(queryPropertiesRangeDomain);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        
        PrintWriter pw = null;
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            while(results.hasNext()){
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                RDFNode domain = rs.get("domain");
                RDFNode range = rs.get("range");
                pw.write("<p>");
                pw.write("<table>");  
                pw.write("<caption>Propiedad: "+s+"</caption>");
                    pw.write("<tr>");
                        pw.write("<td id='title_table'><b>Property:</b></td>");
                        pw.write("<td><label>"+s+"</label></td>");
                    pw.write("</tr>");
                    pw.write("<tr>");
                        pw.write("<td id='title_table'><b>Domain:</b></td>");
                        pw.write("<td>"+domain+"</td>");
                    pw.write("</tr>");
                    pw.write("<tr>");
                        pw.write("<td id='title_table'><b>Range:</b></td>");
                        pw.write("<td>"+range+"</td>");
                    pw.write("</tr>");
                pw.write("</table>");    
                pw.write("</p>");
                    
            }
        } catch (IOException e) {
            res = "La consulta no ha podido realizarse por " + e;
        } finally {
            qexec.close();
        }
    }
    
    public void queryClassSubClass(){
        Query query = QueryFactory.create(queryClassSubClass);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        
        PrintWriter pw = null;
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            while(results.hasNext()){
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                RDFNode subClassOf = rs.get("subClassOf");
                pw.write("<p>");
                pw.write("<table>");  
                pw.write("<caption>Class <i>"+s+"</i> SubClassOf <i>"+ subClassOf+"</i></caption>");
                    pw.write("<tr>");
                        pw.write("<td id='title_table'><b>Class:</b></td>");
                        pw.write("<td><label>"+s+"</label></td>");
                    pw.write("</tr>");
                    pw.write("<tr>");
                        pw.write("<td id='title_table'><b>SubClassOf:</b></td>");
                        pw.write("<td>"+subClassOf+"</td>");
                    pw.write("</tr>");
                pw.write("</table>");    
                pw.write("</p>");
                    
            }
        } catch (IOException e) {
            res = "La consulta no ha podido realizarse por " + e;
        } finally {
            qexec.close();
        }
    }
    
    public void queryAll(){
        Query query = QueryFactory.create(queryAll);
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
                    pw.write("</tr>");
                pw.write("</table>");
                
            }
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryClassPerson(){
        Query query = QueryFactory.create(queryPerson);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "foaf:Person";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties:</b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryClassProvince(){
        Query query = QueryFactory.create(queryProvince);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "place:Province";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryCountry(){
        Query query = QueryFactory.create(queryCountry);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "place:Country";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryCanton(){
        Query query = QueryFactory.create(queryCanton);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:Canton";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryCenso(){
        Query query = QueryFactory.create(queryCenso);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:Canton";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryDatosPersonales(){
        Query query = QueryFactory.create(queryDatosPersonales);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:DatosPersonales";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryDiscapacidad(){
        Query query = QueryFactory.create(queryDiscapacidad);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:Discapacidad";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryFamilia(){
        Query query = QueryFactory.create(queryFamilia);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:Familia";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryMiembros(){
        Query query = QueryFactory.create(queryMiembros);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:Miembros";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryNivelDeInstruccion(){
        Query query = QueryFactory.create(queryNivelDeInstruccion);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:NiveldeInstruccion";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryPoblacion(){
        Query query = QueryFactory.create(queryPoblacion);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:Poblacion";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    public void queryUsaTecnology(){
        Query query = QueryFactory.create(queryUsaTecnology);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query, defaultGraph);
        String res = "";
        PrintWriter pw = null;
        String ClassPerson = "pobl:UsaTecnology";
        try {
            ResultSet results = qexec.execSelect();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            pw = response.getWriter();
            pw.write("<b>Class: </b>"+ClassPerson+"<br />");
            pw.write("<b>Properties: </b>");
            while(results.hasNext()){
                //System.out.println(results.next());
                QuerySolution rs = results.nextSolution();
                RDFNode s = rs.get("s");
                        pw.write(s+" ║ ");
                    
            }
            pw.write("<br />");
        } catch (Exception e) {
            res = "La consulta no ha podido realizarse " + e;
        } finally {
            qexec.close();
        }
        
    }
    
}
