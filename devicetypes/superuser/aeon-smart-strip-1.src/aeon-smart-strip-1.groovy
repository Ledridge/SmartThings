/**
 *  
 *  Device Maker
 *
 *  Copyright 2014 Cooper Lee

 *	Special thanks to @jwu & for reference material
 *
 */
metadata {
	definition (name: "Aeon Smart Strip 1", author: "Cooper G Lee") {
		capability "Energy Meter"
		capability "Refresh"
		capability "Power Meter"
		capability "Switch"
		capability "Configuration"
		capability "Polling"

		attribute "power1", "string"
		attribute "power2", "string"
		attribute "power3", "string"
		attribute "power4", "string"
		attribute "power5", "string"
		attribute "power6", "string"
		attribute "energy1", "string"
		attribute "energy2", "string"
		attribute "energy3", "string"
		attribute "energy4", "string"
		attribute "energy5", "string"
		attribute "energy6", "string"
		attribute "outlet1", "string"
		attribute "outlet2", "string"
		attribute "switch1", "string"
		attribute "switch2", "string"
		attribute "switch3", "string"
		attribute "switch4", "string"
		attribute "switch5", "string"
		attribute "switch6", "string"
		attribute "switch7", "string"

		attribute "Mode", "string"


		command "on"
		command "off"

		command "on1"
		command "off1"
		command "on2"
		command "off2"
		command "on3"
		command "off3"
		command "on4"
		command "off4"

		command "on7"
		command "off7"
		
		command "configure"


		fingerprint deviceId: "0x0001", inClusters: "0x25 0x32 0x27 0x70 0x85 0x72 0x86 0x60 0xEF 0x82"

	}

	simulator {
		// TODO: define status and reply messages here
	}

	preferences {
		input name:"updateFrequency", type:"number", title:"Have the strip send updates every how many seconds?", defaultValue:15
		input name:"switchAll",  type:"bool", title:"Use All On / All Off ? (Includes/Excludes from Z-Wave All On/Off)"
		input name:"outletWatts",  type:"number", title:"What wattage threshold for Outlets to turn On/Off (switch5/switch6)?", defaultValue:1
	}


	tiles {
		standardTile("switch", "device.switch",canChangeIcon: false) {
                        state "on", label: "strip", action: "off", icon: "http://cdn.flaticon.com/png/256/25706.png", backgroundColor: "#79b821"
                        state "off", label: "strip", action: "on", icon: "http://cdn.flaticon.com/png/256/25706.png", backgroundColor: "#ffffff"
                }

		standardTile("outlet1", "device.outlet1",canChangeIcon: false) {
                        state "on", label: "outlet1", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "outlet1", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }
        standardTile("outlet2", "device.outlet2",canChangeIcon: false) {
                        state "on", label: "outlet2", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "outlet2", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }


		standardTile("switch1", "device.switch1",canChangeIcon: false) {
                        state "on", label: "switch1", action: "off1", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "switch1", action: "on1", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }
        standardTile("switch2", "device.switch2",canChangeIcon: false) {
                        state "on", label: "switch2", action: "off2", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "switch2", action: "on2", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }
        standardTile("switch3", "device.switch3",canChangeIcon: false) {
                        state "on", label: "switch3", action: "off3", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "switch3", action:"on3", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }
        standardTile("switch4", "device.switch4",canChangeIcon: false) {
                        state "on", label: "switch4", action: "off4", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "switch4", action:"on4", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }

        standardTile("switch5", "device.switch5",canChangeIcon: false) {
                        state "on", label: "Outlet1 (sw5)", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "Outlet1 (sw5)", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }
        standardTile("switch6", "device.switch6",canChangeIcon: false) {
                        state "on", label: "Outlet2 (sw6)", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "Outlet2 (sw6)", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }
        standardTile("switch7", "device.switch7",canChangeIcon: false) {
                        state "on", label: "All On (sw7)", action: "off7", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                        state "off", label: "All Off (sw7)", action: "on7", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
                }



        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
                        state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
                }
        standardTile("reset", "device.switch", inactiveLabel: false, decoration: "flat") {
        				state "default", label:"reset kWh", action:"reset"
                }
        standardTile("configure", "device.switch", inactiveLabel: false, decoration: "flat") {
        				state "default", label:"", action:"configure", icon:"st.secondary.configure"
                }

        valueTile("power", "device.power", decoration: "flat") {
			state "default", label:'Power Strip ${currentValue} W'
		}
		valueTile("energy", "device.energy", decoration: "flat") {
			state "default", label:'Power Strip ${currentValue} kWh'
		}

        valueTile("power1", "device.power1", decoration: "flat") {
			state "default", label:'Power 1\n${currentValue} W'
		}
		valueTile("energy1", "device.energy1", decoration: "flat") {
			state "default", label:'Energy 1\n${currentValue} kWh'
		}
        valueTile("power2", "device.power2", decoration: "flat") {
			state "default", label:'Power 2\n${currentValue} W'
		}
		valueTile("energy2", "device.energy2", decoration: "flat") {
			state "default", label:'Energy 2\n${currentValue} kWh'
		}
        valueTile("power3", "device.power3", decoration: "flat") {
			state "default", label:'Power 3\n${currentValue} W'
		}
		valueTile("energy3", "device.energy3", decoration: "flat") {
			state "default", label:'Energy 3\n${currentValue} kWh'
		}
        valueTile("power4", "device.power4", decoration: "flat") {
			state "default", label:'Power 4\n${currentValue} W'
		}
		valueTile("energy4", "device.energy4", decoration: "flat") {
			state "default", label:'Energy 4\n${currentValue} kWh'
		}
        valueTile("power5", "device.power5", decoration: "flat") {
			state "default", label:'Power 5\n${currentValue} W'
		}
		valueTile("energy5", "device.energy5", decoration: "flat") {
			state "default", label:'Energy 5\n${currentValue} kWh'
		}
        valueTile("power6", "device.power6", decoration: "flat") {
			state "default", label:'Power 6\n${currentValue} W'
		}
		valueTile("energy6", "device.energy6", decoration: "flat") {
			state "default", label:'Energy 6\n${currentValue} kWh'
		}

		valueTile("Mode", "device.Mode", decoration: "flat") {
			state "default", label:'Switch All Mode ${currentValue}'
		}




        main(["switch", "power", "energy"])
        details(["switch", "power", "energy","outlet1", "power1", "energy1", "outlet2", "power2", "energy2", "switch1", "power3", "energy3","switch2", "power4", "energy4","switch3", "power5", "energy5","switch4", "power6", "energy6","switch5","switch6","switch7","Mode", "configure","reset","refresh"])

	}
}

def parse(String description) {
    def results = []
    def cmd = zwave.parse(description, [0x60:3, 0x25:1, 0x32:1, 0x70:1 ])
    if (cmd) { results = createEvent(zwaveEvent(cmd)) }
    return results
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
//    log.debug "Strip (or sw1) Basic - $cmd ${cmd?.value}"
    def map = []; def value;
    if(cmd.value==255) { value="on" } else { value="off" }
 	for ( i in 1..4 ) {
    	def sw = device.currentValue("switch$i")
//        log.debug "SW$i $sw"
        if(device.currentValue("switch$i")=="on") value="on"
	}
    map = [name: "switch", value:value, type: "digital"]
	map
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
    log.debug "Strip BINARY (all on/off or button) - $cmd ${cmd?.value}"
    def map = []; def value
    if(cmd.value==255) { value="on" } else { value="off" }
    map = [name: "switch", value:value, type: "digital"]
    if(switchAll=="true") {
        for ( i in 1..4 ) { sendEvent(name: "switch$i", value:value, type: "digital") }
	}
	map
}


def zwaveEvent(physicalgraph.zwave.commands.switchallv1.SwitchAllReport cmd) {
//    log.debug "Switch All - $cmd ${cmd?.mode}"
    def value
    if(cmd.mode==255) { value="on" } else { value="off" }
    return [name:"Mode", value:value]
}


def zwaveEvent(physicalgraph.zwave.commands.meterv1.MeterReport cmd) {
//	log.debug "Standard v1 Meter Report $cmd"
    def map = []

	if (cmd.scale == 0) {
    	map = [ name: "energy", value: cmd.scaledMeterValue, unit: "kWh" ]
    }
    else if (cmd.scale == 2) {
    	map = [ name: "power", value: Math.round(cmd.scaledMeterValue), unit: "W" ]
    }

    map
}

def zwaveEvent(int endPoint, physicalgraph.zwave.commands.meterv1.MeterReport cmd) {
//	 log.debug "V1 Report EndPoint $endPoint, MeterReport $cmd  scale ${cmd?.scale}"
    def map = []

    if (cmd?.scale == 0) {
    	map = [ name: "energy" + endPoint, value: cmd.scaledMeterValue, unit: "kWh"]
        if(endPoint < 3) { 
        	log.debug "Outlet $endPoint Energy Info - $cmd.scaledMeterValue"
            def outlet = "outlet" + endPoint
        	state["$outlet"] = cmd.scaledMeterValue
            if(cmd.scaledMeterValue > 0) {
        	
            } else {
			
            }
        }

    }
    else if (cmd?.scale == 2) {
    	map = [  name: "power" + endPoint, value: Math.round(cmd.scaledMeterValue), unit: "W" ]
        if(endPoint < 3) { 
        	log.debug "Outlet $endPoint Power Info - $cmd.scaledMeterValue"
        	def outletWatts = device.currentValue("outletWatts") as BigDecimal ?: 1
    		if(cmd.scaledMeterValue > outletWatts) {
        		sendEvent( name: "outlet" + endPoint, value: "on" )
        		sendEvent( name: "switch" + (endPoint+4), value: "on" )

            } else {
				sendEvent( name: "outlet" + endPoint, value: "off" )
        		sendEvent( name: "switch" + (endPoint+4), value: "off" )
			}
        }
    }

    map
}

def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
//    log.debug "Mv3 $cmd"

    def map = [ name: "switch$cmd.sourceEndPoint" ]
    if (cmd.commandClass == 37){
    	if (cmd.parameter == [0]) {
        	map.value = "off"
        }
        if (cmd.parameter == [255]) {
            map.value = "on"
        	sendEvent(name:"switch", value:"on", displayed:false)
        }
        map
    }
    else if (cmd.commandClass == 50) {
        def hex1 = { n -> String.format("%02X", n) }
        def desc = "command: ${hex1(cmd.commandClass)}${hex1(cmd.command)}, payload: " + cmd.parameter.collect{hex1(it)}.join(" ")
        //log.debug "ReParse command as specifc endpoint"
        zwaveEvent(cmd.sourceEndPoint, zwave.parse(desc, [ 0x25:1, 0x32:1, 0x70:1 ]))
    }
}

def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCapabilityReport cmd) {
//	  [50, 37, 32], dynamic: false, endPoint: 1, genericDeviceClass: 16, specificDeviceClass: 1)
    log.debug "mc v3 report $cmd"
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
	log.debug "Configuration Report for parameter ${cmd.parameterNumber}: Value is ${cmd.configurationValue}, Size is ${cmd.size}"
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
        // Handles all Z-Wave commands we aren't interested in
        [:]
    log.debug "Capture All $cmd"
}


//def on1() { swOn(1) }; def off1() { swOff(1) }
def on2() { swOn(2) }; def off2() { swOff(2) }
def on3() { swOn(3) }; def off3() { swOff(3) }
def on4() { swOn(4) }; def off4() { swOff(4) }

def on1() {
    	delayBetween([
		//zwave.switchAllV1.switchAllSet(mode:0).format(),
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:1, commandClass:37, command:1, parameter:[255]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:1, commandClass:37, command:2).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:2, commandClass:37, command:2).format(),
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:3, commandClass:50, command:1, parameter:[0]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:3, commandClass:50, command:1, parameter:[16]).format(),
	])
}

def off1() {
    	delayBetween([
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:1, commandClass:37, command:1, parameter:[0]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:1, commandClass:37, command:2).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:2, commandClass:37, command:2).format(),
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:3, commandClass:50, command:1, parameter:[0]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:3, commandClass:50, command:1, parameter:[16]).format(),
	])
}



def swOn(port) {
	delayBetween([
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port, commandClass:37, command:1, parameter:[255]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port, commandClass:37, command:2, parameter:[0]).format(),
        "delay 1200",
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port+2, commandClass:50, command:1, parameter:[16]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port+2, commandClass:50, command:1, parameter:[0]).format(),
	])
}

def swOff(port) {
	delayBetween([
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port, commandClass:37, command:1, parameter:[0]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port, commandClass:37, command:2, parameter:[0]).format(),
        "delay 1200",
        zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port+2, commandClass:50, command:1, parameter:[16]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:port+2, commandClass:50, command:1, parameter:[0]).format(),
	])
}





// handle commands
def refresh() {
	log.debug "Refresh $device.label"
    poll()
}





def on() {
	log.debug "Strip On"
    delayBetween([
		zwave.switchAllV1.switchAllOn().format(),
		zwave.switchAllV1.switchAllGet().format(),
		zwave.basicV1.basicGet().format(),
        zwave.switchBinaryV1.switchBinaryGet().format(),
	    zwave.meterV2.meterGet(scale:0).format(),
    	zwave.meterV2.meterGet(scale:2).format(),

	],300)
}

def off() {
	log.debug "Strip Off"
	delayBetween([
		zwave.switchAllV1.switchAllOff().format(),
		zwave.switchAllV1.switchAllGet().format(),
		zwave.basicV1.basicGet().format(),
        zwave.switchBinaryV1.switchBinaryGet().format(),
	    zwave.meterV2.meterGet(scale:0).format(),
    	zwave.meterV2.meterGet(scale:2).format(),

	],300)
}



def configure() {
	log.debug "Executing 'configure'"
    def switchAllmode
    if(switchAll=="true") { switchAllmode = 255 } else { switchAllmode=0 }
    def updateFrequency = device.currentValue("updateFrequency") as BigDecimal
    if(!updateFrequency) updateFrequency = 15
    log.debug "Configuring $device.label - SW All: $switchAllmode ($switchAll) Update Frequency: $updateFrequency"
    def cmds = []
    	cmds << zwave.configurationV1.configurationSet(parameterNumber:101, size:4, configurationValue: [ 0, 0, 127, 127 ]).format()
        cmds << zwave.configurationV1.configurationSet(parameterNumber:111, size:4, scaledConfigurationValue: updateFrequency).format()
        cmds << zwave.configurationV1.configurationSet(parameterNumber:112, size:4, scaledConfigurationValue: updateFrequency).format()
        cmds << zwave.configurationV1.configurationSet(parameterNumber:113, size:4, scaledConfigurationValue: updateFrequency).format()
        cmds << zwave.configurationV1.configurationSet(parameterNumber:3, configurationValue: [3]).format()
        cmds << zwave.configurationV1.configurationSet(parameterNumber:4, configurationValue: [0]).format()
        cmds << zwave.switchAllV1.switchAllSet(mode:switchAllmode).format()
        cmds << zwave.configurationV1.configurationGet().format()
    for ( i in 1..6 ) {
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:50, command:1, parameter:[0]).format()
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:50, command:1, parameter:[16]).format()
		}
	delayBetween(cmds,150)

}



//reset
def reset() {
	def cmds = []
    cmds << zwave.meterV2.meterReset().format()
    cmds << zwave.meterV2.meterGet(scale:0).format()
    for ( i in 1..6 ) 
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:50, command:1, parameter:[0]).format()

	delayBetween(cmds)
}



def poll() {
	log.debug "<FONT COLOR=RED>Polling Powerstrip - $device.label</FONT>"
    def cmds = []
	for ( i in 1..6 ) {
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:37, command:2, parameter:[0]).format()
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:50, command:1, parameter:[0]).format()
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:50, command:1, parameter:[16]).format()
	}
	    cmds << zwave.meterV2.meterGet(scale:0).format()
    	cmds << zwave.meterV2.meterGet(scale:2).format()

	delayBetween(cmds,400)
}

def on7() {
    log.debug "All On"
    def cmds = []
        for ( i in 1..4 ) {
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:37, command:1, parameter:[255]).format()
            cmds << "delay 200"
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i+2, commandClass:50, command:1, parameter:[0]).format()
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i+2, commandClass:50, command:1, parameter:[16]).format()
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:37, command:2, parameter:[0]).format()
        }
	delayBetween(cmds,300)
    sendEvent(name:"switch7", value:"on")
}


def off7() {
        log.debug "All Off"
    def cmds = []
        for ( i in 1..4 ) {
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:37, command:1, parameter:[0]).format()
            cmds << "delay 200"
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i+2, commandClass:50, command:1, parameter:[0]).format()
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i+2, commandClass:50, command:1, parameter:[16]).format()
            cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:i, commandClass:37, command:2, parameter:[0]).format()
        }
	delayBetween(cmds,300)
    sendEvent(name:"switch7", value:"off")
}
