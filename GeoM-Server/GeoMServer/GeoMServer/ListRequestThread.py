import threading
from xml.dom import minidom
from ParserXML import ParserXML
import time
import socket

class ListRequestThread(threading.Thread):
    
    def __init__(self, sd, ID, conn, addr):
        threading.Thread.__init__(self)
        self.sd = sd
        self.ID = ID
        self.conn = conn
        self.addr = addr
        self.time = 5
        
    def run(self):
        try:
            print("Connected by", self.addr)
            pxml = ParserXML()
            self.send(pxml.getDOMResponse("Connected"))
            msg = self.conn.recv(1024).decode('utf-8').strip() # ricevo numero di trasporti da inviare al client
            doc = pxml.toDOMObject(msg)
            richiesta = pxml.getLimitOffsetAndPTtype() # ottengo una tupla (tipo, limit, offset)
            self.sd.getDOMTransportsList(richiesta[0], )
            
            msg = self.sd.getDOMTransportsList() # Creo lista mezzi     
            self.send(msg)       
            self.conn.close()
            print("Connessione chiusa correttamente")
        except ConnectionResetError:
            print("socked closed by client")
                

    def send(self, mex):
        # se il messaggio è di tipo Document, prima lo trasformo in una stringa XML
        if isinstance(mex, minidom.Document):
            mex = mex.toxml()
            mex = mex.replace("\n", "")
        mex += "\r\n"
        self.conn.send(mex.encode('utf-8'))