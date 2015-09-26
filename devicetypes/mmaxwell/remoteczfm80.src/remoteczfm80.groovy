/* Remotec ZFM-80 specific device V1.2
 *
 * Variation of the stock SmartThings Relay Switch
 *	--auto re-configure after setting preferences
 *	--preference settings for switch type and automatic shutoff features.
 *
 *		
 * Mike Maxwell
 * madmax98087@yahoo.com
 * 2015-02-16
 *
	change log
 		2015-02-16 added delay between configuration changes, helps with devices further away from the hub.
        2015-02-21 fixed null error on initial install
*/

metadata {

	definition (name: "remotecZFM80", namespace: "mmaxwell", author: "mike maxwell") {
		capability "Actuator"
        capability "Door Control"
		capability "Switch"
        capability "Momentary"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
        capability "Contact Sensor"
		capability "Relay Switch"

		fingerprint deviceId: "0x1003", inClusters: "0x20, 0x25, 0x27, 0x72, 0x86, 0x70, 0x85"
	}
    preferences {
        input name: "param1", type: "enum", title: "Set external switch mode:", description: "Switch type", required: true, options:["Disabled","Momentary NO","Momentary NC","Toggle NO","Toggle NC"]
       	input name: "param2", type: "enum", title: "Auto shutoff minutes:", description: "Minutes?", required: false, options:["Never","1","5","30","60","90","120","240"]
    }

	// simulator metadata
	simulator {
		status "on":  "command: 2003, payload: FF"
		status "off": "command: 2003, payload: 00"

		// reply messages
		reply "2001FF,delay 100,2502": "command: 2503, payload: FF"
		reply "200100,delay 100,2502": "command: 2503, payload: 00"
	}

	// tile definitions
	tiles {
		standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main "switch"
		details(["switch","refresh"])
	}
}

def installed() {
	zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
}

def parse(String description) {
	def result = null
	def cmd = zwave.parse(description, [0x20: 1, 0x70: 1])
	if (cmd) {
		result = createEvent(zwaveEvent(cmd))
	}
	if (result?.name == 'hail' && hubFirmwareLessThan("000.011.00602")) {
		result = [result, response(zwave.basicV1.basicGet())]
		log.debug "Was hailed: requesting state update"
	} else {
		log.debug "Parse returned ${result?.descriptionText}"
	}
	return result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	[name: "switch", value: cmd.value ? "on" : "off", type: "physical"]
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
	[name: "switch", value: cmd.value ? "on" : "off", type: "digital"]
}

//def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
//	def value = "when off"
//	if (cmd.configurationValue[0] == 1) {value = "when on"}
//	if (cmd.configurationValue[0] == 2) {value = "never"}
//	[name: "indicatorStatus", value: value, display: false]
//}

def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
	[name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false]
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
	if (state.manufacturer != cmd.manufacturerName) {
		updateDataValue("manufacturer", cmd.manufacturerName)
	}

	final relays = [
	    //[manufacturerId:0x0113, productTypeId: 0x5246, productId: 0x3133, productName: "Evolve LFM-20"],
        //[manufacturerId:0x0113, productTypeId: 0x5246, productId: 0x3133, productName: "Linear FS20Z-1"],
		[manufacturerId:0x5254, productTypeId: 0x8000, productId: 0x0002, productName: "Remotec ZFM-80"]
	]

	def productName  = null
	for (it in relays) {
		if (it.manufacturerId == cmd.manufacturerId && it.productTypeId == cmd.productTypeId && it.productId == cmd.productId) {
			productName = it.productName
			break
		}
	}

	if (productName) {
		//log.debug "Relay found: $productName"
		updateDataValue("productName", productName)
	}
	else {
		//log.debug "Not a relay, retyping to Z-Wave Switch"
		setDeviceType("Z-Wave Switch")
	}
	[name: "manufacturer", value: cmd.manufacturerName]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	// Handles all Z-Wave commands we aren't interested in
	[:]
}

def on() {
	delayBetween([
		zwave.basicV1.basicSet(value: 0xFF).format(),
		zwave.switchBinaryV1.switchBinaryGet().format()
	])
}

def off() {
	delayBetween([
		zwave.basicV1.basicSet(value: 0x00).format(),
		zwave.switchBinaryV1.switchBinaryGet().format()
	])
}

def poll() {
	zwave.switchBinaryV1.switchBinaryGet().format()
}

def refresh() {
	delayBetween([
		zwave.switchBinaryV1.switchBinaryGet().format(),
		zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
	])
}
//capture preference changes
def updated() {
    //log.debug "before settings: ${settings.inspect()}, state: ${state.inspect()}" 
    //"Disabled","Momentary NO","Momentary NC","Toggle NO","Toggle NC"
    
    //external switch function settings
    def Short p1 = 0
    switch (settings.param1) {
		case "Disabled":
			p1 = 0
            break
		case "Momentary NO":
			p1 = 1
            break
		case "Momentary NC":
			p1 = 2
            break
		case "Toggle NO":
			p1 = 3
            break
		case "Toggle NC":
			p1 = 4
            break
	}    
    
    
	//auto off
    def Short p2 = 0
    if ("${settings.param2}" == "Never") {
    	p2 = 0
    } else {
    	p2 = (settings.param2 ?: 0).toInteger()
    }
    
    if (p1 != state.param1)	{
        state.param1 = p1 
        return response(zwave.configurationV1.configurationSet(parameterNumber: 1, size: 1, configurationValue: [p1]).format())
    }
    
	if (p2 != state.param2)	{
        state.param2 = p2
        if (p2 == 0) {
       		return response (delayBetween([
				zwave.configurationV1.configurationSet(parameterNumber: 2, size: 1, configurationValue: [0]).format(),
        		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, configurationValue: [0]).format(),
        		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, configurationValue: [0]).format()
			]))
        } else {
        	return response (delayBetween([
				zwave.configurationV1.configurationSet(parameterNumber: 2, size: 1, configurationValue: [p2]).format(),
        		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, configurationValue: [232]).format(),
        		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, configurationValue: [0]).format()
			]))
        }
    }
	
	//log.debug "after settings: ${settings.inspect()}, state: ${state.inspect()}"
}

def configure() {
	delayBetween([
		zwave.configurationV1.configurationSet(parameterNumber: 1, size: 1, configurationValue: [3]).format(),
		zwave.configurationV1.configurationSet(parameterNumber: 2, size: 1, configurationValue: [0]).format(),
        zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, configurationValue: [0]).format(),
        zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, configurationValue: [0]).format()
	])
}