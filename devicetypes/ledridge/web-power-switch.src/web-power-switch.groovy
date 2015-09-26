/**
 *  Web Power Switch 7
 */
 
import com.google.common.base.Splitter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


preferences {
        input("ip", "string", title:"IP Address", description: "192.168.1.3", defaultValue: "192.168.1.3" ,required: true, displayDuringSetup: true)
        input("port", "string", title:"Port", description: "80", defaultValue: "80" , required: true, displayDuringSetup: true)
        input("username", "string", title:"Username", description: "admin", defaultValue: "admin" , required: true, displayDuringSetup: true)
        input("password", "password", title:"Password", description: "password", defaultValue: "password" , required: true, displayDuringSetup: true)
        
        input("port1name", "string", title:"Port1 Name", description: "Name for port", defaultValue: "Port 1" , required: true, displayDuringSetup: true)
        input("port2name", "string", title:"Port2 Name", description: "Name for port", defaultValue: "Port 2" , required: true, displayDuringSetup: true)
        input("port3name", "string", title:"Port3 Name", description: "Name for port", defaultValue: "Port 3" , required: true, displayDuringSetup: true)
        input("port4name", "string", title:"Port4 Name", description: "Name for port", defaultValue: "Port 4" , required: true, displayDuringSetup: true)
        input("port5name", "string", title:"Port5 Name", description: "Name for port", defaultValue: "Port 5" , required: true, displayDuringSetup: true)
        input("port6name", "string", title:"Port6 Name", description: "Name for port", defaultValue: "Port 6" , required: true, displayDuringSetup: true)
        input("port7name", "string", title:"Port7 Name", description: "Name for port", defaultValue: "Port 7" , required: true, displayDuringSetup: true)
        input("port8name", "string", title:"Port8 Name", description: "Name for port", defaultValue: "Port 8" , required: true, displayDuringSetup: true)
		}

metadata {
	definition (name: "Web Power Switch", namespace: "Ledridge", author: "Ledridge") {
    
		capability "Polling"
		capability "Refresh"
        capability "Switch"
        
        command "OA1ON" 
        command "OA1OFF"
        command "OA1CCL"

        command "OA2ON" 
        command "OA2OFF"
        command "OA2CCL"

        command "OA3ON" 
        command "OA3OFF"
        command "OA3CCL"

        command "OA4ON" 
        command "OA4OFF"
        command "OA4CCL"

        command "OA5ON" 
        command "OA5OFF"
        command "OA5CCL"

        command "OA6ON" 
        command "OA6OFF"
        command "OA6CCL"

        command "OA7ON" 
        command "OA7OFF"
        command "OA7CCL"

        command "OA8ON" 
        command "OA8OFF"
        command "OA8CCL"
        
        command "OutletAction", ["number", "string"]
        command "OutletStatus", ["number"]
        command "OutletName", ["number"]
		}

	simulator {
		// TODO: define status and reply messages here
		}

	tiles {
    
    	valueTile("Label1", "device.Label1", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet1", "device.Outlet1", width: 1, height: 1) {
            state "off", action: "OA1ON", label: 'Off', backgroundColor: "#ffffff", nextState: "on"
            state "on" , action: "OA1OFF", label: 'On', backgroundColor: "#79b821", nextState: "off"
            }
        standardTile("Cycle1", "device.Cycle1", width: 1, height: 1) {
            state "off", action: "OA1CCL", label: 'Off', backgroundColor: "#ffffff", nextState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", nextState: "off", icon: "st.secondary.refresh"
            }

    	valueTile("Label2", "device.Label2", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet2", "device.Outlet2", width: 1, height: 1) {
            state "off", action: "OA2ON", label: 'Off', backgroundColor: "#ffffff", ne2tState: "on"
            state "on" , action: "OA2OFF", label: 'On', backgroundColor: "#79b821", ne2tState: "off"
            }
        standardTile("Cycle2", "device.Cycle2", width: 1, height: 1) {
            state "off", action: "OA2CCL", label: 'Off', backgroundColor: "#ffffff", ne2tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne2tState: "off", icon: "st.secondary.refresh"
            }

    	valueTile("Label3", "device.Label3", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet3", "device.Outlet3", width: 1, height: 1) {
            state "off", action: "OA3ON", label: 'Off', backgroundColor: "#ffffff", ne3tState: "on"
            state "on" , action: "OA3OFF", label: 'On', backgroundColor: "#79b821", ne3tState: "off"
            }
        standardTile("Cycle3", "device.Cycle3", width: 1, height: 1) {
            state "off", action: "OA3CCL", label: 'Off', backgroundColor: "#ffffff", ne3tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne3tState: "off", icon: "st.secondary.refresh"
            }

        valueTile("Label4", "device.Label4", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet4", "device.Outlet4", width: 1, height: 1) {
            state "off", action: "OA4ON", label: 'Off', backgroundColor: "#ffffff", ne4tState: "on"
            state "on" , action: "OA4OFF", label: 'On', backgroundColor: "#79b821", ne4tState: "off"
            }
        standardTile("Cycle4", "device.Cycle4", width: 1, height: 1) {
            state "off", action: "OA4CCL", label: 'Off', backgroundColor: "#ffffff", ne4tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne4tState: "off", icon: "st.secondary.refresh"
            }

        valueTile("Label5", "device.Label5", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet5", "device.Outlet5", width: 1, height: 1) {
            state "off", action: "OA5ON", label: 'Off', backgroundColor: "#ffffff", ne5tState: "on"
            state "on" , action: "OA5OFF", label: 'On', backgroundColor: "#79b821", ne5tState: "off"
            }
        standardTile("Cycle5", "device.Cycle5", width: 1, height: 1) {
            state "off", action: "OA5CCL", label: 'Off', backgroundColor: "#ffffff", ne5tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne5tState: "off", icon: "st.secondary.refresh"
            }

        valueTile("Label6", "device.Label6", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet6", "device.Outlet6", width: 1, height: 1) {
            state "off", action: "OA6ON", label: 'Off', backgroundColor: "#ffffff", ne6tState: "on"
            state "on" , action: "OA6OFF", label: 'On', backgroundColor: "#79b821", ne6tState: "off"
            }
        standardTile("Cycle6", "device.Cycle6", width: 1, height: 1) {
            state "off", action: "OA6CCL", label: 'Off', backgroundColor: "#ffffff", ne6tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne6tState: "off", icon: "st.secondary.refresh"
            }

        valueTile("Label7", "device.Label7", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet7", "device.Outlet7", width: 1, height: 1) {
            state "off", action: "OA7ON", label: 'Off', backgroundColor: "#ffffff", ne7tState: "on"
            state "on" , action: "OA7OFF", label: 'On', backgroundColor: "#79b821", ne7tState: "off"
            }
        standardTile("Cycle7", "device.Cycle7", width: 1, height: 1) {
            state "off", action: "OA7CCL", label: 'Off', backgroundColor: "#ffffff", ne7tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne7tState: "off", icon: "st.secondary.refresh"
            }

        valueTile("Label8", "device.Label8", decoration: "flat") {state "default", label:'${currentValue}'}
        standardTile("Outlet8", "device.Outlet8", width: 1, height: 1) {
            state "off", action: "OA8ON", label: 'Off', backgroundColor: "#ffffff", ne8tState: "on"
            state "on" , action: "OA8OFF", label: 'On', backgroundColor: "#79b821", ne8tState: "off"
            }
        standardTile("Cycle8", "device.Cycle8", width: 1, height: 1) {
            state "off", action: "OA8CCL", label: 'Off', backgroundColor: "#ffffff", ne8tState: "on", icon: "st.secondary.refresh"
            state "on" , label: 'On', backgroundColor: "#79b821", ne8tState: "off", icon: "st.secondary.refresh"
            }
        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat") {
        	state "default", action:"refresh.refresh", icon: "st.secondary.refresh"
        	}
    
        main "Outlet1"
        details([ "Label1", "Outlet1", "Cycle1"
                    ,"Label2", "Outlet2", "Cycle2"
                    ,"Label3", "Outlet3", "Cycle3"
                    ,"Label4", "Outlet4", "Cycle4"
                    ,"Label5", "Outlet5", "Cycle5"
                    ,"Label6", "Outlet6", "Cycle6"
                    ,"Label7", "Outlet7", "Cycle7"
                    ,"Label8", "Outlet8", "Cycle8", "refresh"])
    		}
}

// ------------------------------------------------------------------

// parse events into attributes
def parse(String description) {
    def map = [:]
    def descMap = parseDescriptionAsMap(description)
    //log.debug "descMap: ${descMap}"
    
    //log.debug description.decodeBase64().substring(50,10)
    
    def body = new String(descMap["body"].decodeBase64())
    //log.debug "body: ${body}"

	def hexString = body.tokenize()
	//log.debug "0: ${hexString[0]}"
    //log.debug "1: ${hexString[1]}"
    //log.debug "2: ${hexString[2]}"
    //log.debug "3: ${hexString[3]}"
    //log.debug "4: ${hexString[4]}"
    //log.debug "5: ${hexString[5]}"
    //log.debug "6: ${hexString[6]}"
    //log.debug "7: ${hexString[7]}"
    //log.debug "8: ${hexString[8]}"
    
    if (hexString[1].contains("status"))
    {

        def a = hexString[3].toString().replace('id="state">','');
        //log.debug "a: ${a}"

        char[] charArray = a.toCharArray();

        //log.debug charArray[0];
        //log.debug charArray[1];


        def b = "${charArray[0]}${charArray[1]}"
        //log.debug "b: ${b}"

        def binaryString = hexToBin(b)
        //log.debug "Binary String: ${binaryString}"

        char[] StatusArray = binaryString.toString().toCharArray();

        //log.debug StatusArray[0];
        //log.debug StatusArray[1];
        //log.debug StatusArray[2];
        //log.debug StatusArray[3];
        //log.debug StatusArray[4];
        //log.debug StatusArray[5];
        //log.debug StatusArray[6];
        //log.debug StatusArray[7];
        
        def o5 = device.latestValue('o5')

        if (StatusArray[7] == "0")
            sendEvent(name: "Outlet1", value: "off")
        else
            sendEvent(name: "Outlet1", value: "on")

        if (StatusArray[6] == "0")
            sendEvent(name: "Outlet2", value: "off")
        else
            sendEvent(name: "Outlet2", value: "on")

        if (StatusArray[5] == "0")
            sendEvent(name: "Outlet3", value: "off")
        else
            sendEvent(name: "Outlet3", value: "on")

        if (StatusArray[4] == "0")
            sendEvent(name: "Outlet4", value: "off")
        else
            sendEvent(name: "Outlet4", value: "on")

        if (StatusArray[3] == "0")
            sendEvent(name: "Outlet5", value: "off")
        else
            sendEvent(name: "Outlet5", value: "on")
            
        if (StatusArray[2] == "0")
            sendEvent(name: "Outlet6", value: "off")
        else
            sendEvent(name: "Outlet6", value: "on")

        if (StatusArray[1] == "0")
            sendEvent(name: "Outlet7", value: "off")
        else
            sendEvent(name: "Outlet7", value: "on")

        if (StatusArray[0] == "0")
            sendEvent(name: "Outlet8", value: "off")
        else
            sendEvent(name: "Outlet8", value: "on")

	}
    else
        log.debug "not status"


    //log.debug "Label1: ${port1name}"
    sendEvent(name: "Label1", value: port1name)

    //log.debug "Label2: ${port2name}"
    sendEvent(name: "Label2", value: port2name)

    //log.debug "Label3: ${port3name}"
    sendEvent(name: "Label3", value: port3name)

    //log.debug "Label4: ${port4name}"
    sendEvent(name: "Label4", value: port4name)

    //log.debug "Label5: ${port5name}"
    sendEvent(name: "Label5", value: port5name)

    //log.debug "Label6: ${port6name}"
    sendEvent(name: "Label6", value: port6name)

    //log.debug "Label7: ${port7name}"
    sendEvent(name: "Label7", value: port7name)

    //log.debug "Label8: ${port8name}"
    sendEvent(name: "Label8", value: port8name)
  
}

// handle commands
def poll() {
	log.debug "Executing 'poll'"
    getRemoteData()
}

def refresh() {
	sendEvent(name: "switch", value: "off")
	log.debug "Executing 'refresh'"
    getRemoteData()
}


def OA1ON() {OutletAction(1,"ON")}
def OA1OFF(){OutletAction(1,"OFF")}
def OA1CCL(){OutletAction(1,"CCL")}

def OA2ON() {OutletAction(2,"ON")}
def OA2OFF(){OutletAction(2,"OFF")}
def OA2CCL(){OutletAction(2,"CCL")}

def OA3ON() {OutletAction(3,"ON")}
def OA3OFF(){OutletAction(3,"OFF")}
def OA3CCL(){OutletAction(3,"CCL")}

def OA4ON() {OutletAction(4,"ON")}
def OA4OFF(){OutletAction(4,"OFF")}
def OA4CCL(){OutletAction(4,"CCL")}

def OA5ON() {OutletAction(5,"ON")}
def OA5OFF(){OutletAction(5,"OFF")}
def OA5CCL(){OutletAction(5,"CCL")}

def OA6ON() {OutletAction(6,"ON")}
def OA6OFF(){OutletAction(6,"OFF")}
def OA6CCL(){OutletAction(6,"CCL")}

def OA7ON() {OutletAction(7,"ON")}
def OA7OFF(){OutletAction(7,"OFF")}
def OA7CCL(){OutletAction(7,"CCL")}

def OA8ON() {OutletAction(8,"ON")}
def OA8OFF(){OutletAction(8,"OFF")}
def OA8CCL(){OutletAction(8,"CCL")}


def OutletStatus(outlet){
	//getRemoteData()
    
    def int o = outlet.toInteger()
    
	log.debug "Get Outlet ${o} Status"
	def outletStatus 
    switch(o)	{
                    case 1:
                    	outletStatus = device.latestValue('Outlet1');
                        break;
                    case 2:
                    	outletStatus = device.latestValue('Outlet2');
                        break;
                    case 3:
                    	outletStatus = device.latestValue('Outlet3');
                        break;
                    case 4:
                    	outletStatus = device.latestValue('Outlet4');
                        break;
                    case 5:
                    	outletStatus = device.latestValue('Outlet5');
                        break;
                    case 6:
                    	outletStatus = device.latestValue('Outlet6');
                        break;
                    case 7:
                    	outletStatus = device.latestValue('Outlet7');
                        break;
                    case 8:
                    	outletStatus = device.latestValue('Outlet8');
                        break;
                    default:
                        outletStatus = "Invalid Outlet Number: ${o}"
                    }

	log.debug "Outlet Status: ${outletStatus}"
	return outletStatus;
	}
    
def OutletName(outlet){
	def int o = outlet.toInteger()

	log.debug "Get Outlet ${o} Name"
	def outletName 
    switch(o)	{
                    case 1:
                    	outletName = port1name
                        break;
                    case 2:
                    	outletName = port2name
                        break;
                    case 3:
                    	outletName = port3name
                        break;
                    case 4:
                    	outletName = port4name
                        break;
                    case 5:
                    	outletName = port5name
                        break;
                    case 6:
                    	outletName = port6name
                        break;
                    case 7:
                    	outletName = port7name
                        break;
                    case 8:
                    	outletName = port8name
                        break;
                    default:
                        outletName = "Invalid Outlet Number: ${o}"
                    }

	log.debug "Outlet Name: ${outletName}"
	return outletName;
	}

def OutletAction(outlet,action){
	log.debug "Send Outlet ${outlet.toInteger().toString()} ${action}"
    def uri = "/outlet?${outlet.toInteger().toString()}=${action}"
    log.debug "URI: ${uri}"
    delayBetween([
    				postAction(uri),
    				getRemoteData()
                 ], 5000);
	}

private getRemoteData() {
	def uri = "/status"
    postAction(uri)
}

// ------------------------------------------------------------------

private postAction(uri){
  setDeviceNetworkId(ip,port)  
  
  def userpass = encodeCredentials(username, password)
  //log.debug("userpass: " + userpass) 
  
  def headers = getHeader(userpass)
  //log.debug("headders: " + headers) 
  
  def hubAction = new physicalgraph.device.HubAction(
    method: "GET",
    path: uri,
    headers: headers
  )
  log.debug("Executing hubAction on " + getHostAddress())
  //log.debug hubAction
  hubAction    
}

// ------------------------------------------------------------------
// Helper methods
// ------------------------------------------------------------------

def parseDescriptionAsMap(description) {
	description.split(",").inject([:]) { map, param ->
		def nameAndValue = param.split(":")
		map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}
}


def toAscii(s){
        StringBuilder sb = new StringBuilder();
        String ascString = null;
        long asciiInt;
                for (int i = 0; i < s.length(); i++){
                    sb.append((int)s.charAt(i));
                    sb.append("|");
                    char c = s.charAt(i);
                }
                ascString = sb.toString();
                asciiInt = Long.parseLong(ascString);
                return asciiInt;
    }

private encodeCredentials(username, password){
	log.debug "Encoding credentials"
	def userpassascii = "${username}:${password}"
    def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    //log.debug "ASCII credentials are ${userpassascii}"
    //log.debug "Credentials are ${userpass}"
    return userpass
}

private getHeader(userpass){
	log.debug "Getting headers"
    def headers = [:]
    headers.put("HOST", getHostAddress())
    headers.put("Authorization", userpass)
    //log.debug "Headers are ${headers}"
    return headers
}

private delayAction(long time) {
	new physicalgraph.device.HubAction("delay $time")
}

private setDeviceNetworkId(ip,port){
  	def iphex = convertIPtoHex(ip)
  	def porthex = convertPortToHex(port)
  	device.deviceNetworkId = "$iphex:$porthex"
  	log.debug "Device Network Id set to ${iphex}:${porthex}"
}

private getHostAddress() {
	return "${ip}:${port}"
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    return hex

}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
    return hexport
}

private String hexToBin(String hex){
    String bin = "";
    String binFragment = "";
    int iHex;
    hex = hex.trim();
    hex = hex.replaceFirst("0x", "");

    for(int i = 0; i < hex.length(); i++){
        iHex = Integer.parseInt(""+hex.charAt(i),16);
        binFragment = Integer.toBinaryString(iHex);

        while(binFragment.length() < 4){
            binFragment = "0" + binFragment;
        }
        bin += binFragment;
    }
    return bin;
}


