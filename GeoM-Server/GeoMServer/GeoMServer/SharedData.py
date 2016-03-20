from xml.dom import minidom
from Database import *
from UserThread import *
from TransportThread import *

class SharedData(object):
    def __init__(self):
        self.prova = "ciao"
        global db
        db = Database()
        # lista trasporti e relativo indice

    def cambiaProva(self):
        self.prova = "cambiato"

    def toDOMObject(self, string):        
        return minidom.parseString(string) # Ritorna oggetto tipo doc

    def readXMLTable(self, filename):        
        doc = minidom.parse(filename)

        # doc.getElementsByTagName returns NodeList
        buses = doc.getElementsByTagName("buses")
        for bus in buses:
            line = bus.getElementsByTagName("line")[0] # obtain the bus line
            print(line.getAttribute("code") + " " + line.firstChild.nodeValue) # print bus code and value of the line element

    def createXMLObj(self):
        doc = minidom.parse(filename)

        # doc.getElementsByTagName returns NodeList
        buses = doc.getElementsByTagName("mezzi")
        bus   = buses.getElementsByTagName("bus")

        linea = bus.getElementsByTagName("linea")
       
        ##### da provare ######
        linea.firstChild.setValue("prova456")
        print(linea.firstChild.value)
        ##### da provare ######
        tratta = bus.getElementsByTagName("tratta")
        
        #modificare linea e anche tratta

        buses.appendChild(bus)
            
        

    def getTransportList(self):
        listaMezzi = list()
        listaMezzi = db.getTransports()

    def addTransport(self, transport):
        global transportList
        transportList = list()
        transportList.append(transport) # aggiungo un elemento ThreadTrasport nella lista
        transportList[-1].start() # parte il threadTransport | -1 -> ultimo elemento

