package classes;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by cryph on 19/04/2016.
 */
public class LoadingThread extends Thread {
    private SharedData sd;
    private Connection conn;

    public LoadingThread(SharedData sd) {
        this.sd = sd;
        this.conn = new Connection("51.254.127.27", 3333); // instanzio oggettoo
    }

    @Override
    public void run() {
        String msgRicevuto = null;

        conn.startConn(); // connessione con il server

        try {
            conn.sendMessage(conn.getDOMType("listRequest")); // invio il tipo di utente
            msgRicevuto = conn.readMessage(); // ricevo "Connected"
            Log.i("sMESSAGE RECEIVED", msgRicevuto);

            conn.sendMessage(conn.getDOMRichiesta(sd.pt_type, "30", Integer.toString(sd.offset))); // invio il tipo di mezzo da caricare
            Log.i("sMESSAGE SENT", Connection.convertDocumentToString(conn.getDOMRichiesta(sd.pt_type, "30", Integer.toString(sd.offset))));

            msgRicevuto = conn.readMessage(); // ricevo la lista dei trasporti
            Document listaPT = Connection.convertStringToDocument(msgRicevuto);
            Log.i("sMESSAGE RECEIVED", msgRicevuto);

            conn.closeConn(); // chiudo la connessione

            // carico la lista in SharedData
            NodeList mezzi = null;
            NodeList chMezzo = null;
            Node mezzo = null;
            String tipo, nome, compagnia, tratta;
            boolean attivo;
            int id;
            // ottengo la lista di tutti gli elementi "mezzo"
            mezzi = listaPT.getElementsByTagName("mezzo");

            // per ogni mezzo
            for (int i=0; i < mezzi.getLength(); i++) {
                mezzo = mezzi.item(i);
                Element elMezzo = (Element) mezzo;
                id = Integer.parseInt(elMezzo.getAttribute("id"));
                tipo = elMezzo.getElementsByTagName("tipo").item(0).getTextContent(); // ottengo il tipo del mezzo (es. bus o treno)
                compagnia = elMezzo.getElementsByTagName("compagnia").item(0).getTextContent(); // ottengo la compagnia
                nome = elMezzo.getElementsByTagName("nome").item(0).getTextContent(); // ottengo il nome del mezzo
                tratta = elMezzo.getElementsByTagName("tratta").item(0).getTextContent(); // ottengo la tratta del mezzo
                attivo = Boolean.parseBoolean(elMezzo.getElementsByTagName("attivo").item(0).getTextContent()); // ottengo se il mezzo è attivo

                if ("bus".equals(tipo)) {
                    sd.busList.add(new PublicTransport(id, tipo, nome, compagnia, tratta, attivo));
                    Log.i("FUNZIONA", "tipo = " + tipo);
                } else if ("treno".equals(tipo)) {
                    sd.trainList.add(new PublicTransport(id, tipo, nome, compagnia, tratta, attivo));
                    Log.i("FUNZIONA", "tipo = " + tipo);
                } else {
                    Log.e("LoadingThread", "Tipo non corrispondente! tipo = " + tipo);
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
