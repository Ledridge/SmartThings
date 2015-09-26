/**
 *  PoolSwitch
 *
 *  Copyright 2014 bigpunk6
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "PowerStrip", author: "Ledridge") {
        capability "Actuator"
		capability "Switch"
		capability "Polling"
		capability "Configuration"
		capability "Refresh"
		capability "Temperature Measurement"
		capability "Sensor"

		attribute "switch1", "string"
		attribute "switch2", "string"
		attribute "switch3", "string"
		attribute "switch4", "string"
		
		command "onMulti"
		command "offMulti"
        command "on1"
		command "off1"
        command "on2"
		command "off2"
        command "on3"
		command "off3"
        command "on4"
		command "off4"
	}

	simulator {
		// TODO: define status and reply messages here
	}
    
	// tile definitions
	tiles {
		standardTile("switch1", "device.switch1",canChangeIcon: true) {
			state "on", label: "switch1", action: "off1", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "off", label: "switch1", action: "on1", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
        standardTile("switch2", "device.switch2",canChangeIcon: true) {
			state "on", label: "switch2", action: "off2", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "off", label: "switch2", action: "on2", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
        standardTile("switch3", "device.switch3",canChangeIcon: true) {
			state "on", label: "switch3", action: "off3", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "off", label: "switch3", action:"on3", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
        standardTile("switch4", "device.switch4",canChangeIcon: true) {
			state "on", label: "switch4", action: "off4", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "off", label: "switch4", action:"on4", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
        
        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        
        standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat") {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}
        
		main(["switch1","switch2","switch3","switch4","temperature"])
        details(["switch1","switch2","switch3","switch4","temperature","refresh"])
	}
}

import physicalgraph.zwave.commands.*

//Parse
def parse(String description) {
	def result = null
	def cmd = zwave.parse(description, [0x20: 1, 0x70: 2, 0x86: 1, 0x60:3, 0x31:1, 0x25:1, 0x81:1])
	if (cmd) {
        if( cmd.CMD == "6006" ) {
            def map = [ name: "switch$cmd.instance" ]
            if (cmd.commandClass == 37){
                if (cmd.parameter == [0]) {
                    map.value = "off"
                }
                if (cmd.parameter == [255]) {
                    map.value = "on"
                }
            }
            result = createEvent(map)
        } else {
		result = createEvent(zwaveEvent(cmd))
        }
	}
	log.debug "Parse returned ${result?.descriptionText}"
	return result
}

//Reports
def zwaveEvent(sensormultilevelv1.SensorMultilevelReport cmd)
{
	def map = [:]
	map.value = cmd.scaledSensorValue.toString()
	map.unit = cmd.scale == 1 ? "F" : "C"
	map.name = "temperature"
	map
}

def zwaveEvent(thermostatsetpointv2.ThermostatSetpointReport cmd)
{
	def map = [:]
	map.value = cmd.scaledValue.toString()
	map.unit = cmd.scale == 1 ? "F" : "C"
	map.displayed = false
	switch (cmd.setpointType) {
		case 1:
			map.name = "poolSetpoint"
			break;
		case 7:
			map.name = "spaSetpoint"
			break;
		default:
			return [:]
	}
	// So we can respond with same format
	state.size = cmd.size
	state.scale = cmd.scale
	state.precision = cmd.precision
	map
}

def zwaveEvent(multichannelv3.MultiInstanceReport cmd) {
    log.debug "$cmd"
}

def zwaveEvent(multichannelv3.MultiChannelCapabilityReport cmd) {
    log.debug "$cmd"
}

def zwaveEvent(multichannelv3.MultiChannelEndPointReport cmd) {
    log.debug "$cmd"
}

def zwaveEvent(multichannelv3.MultiInstanceCmdEncap cmd) {
    log.debug "$cmd"
    def map = [ name: "switch$cmd.instance" ]
        if (cmd.commandClass == 37){
            if (cmd.parameter == [0]) {
                map.value = "off"
            }
            if (cmd.parameter == [255]) {
                map.value = "on"
            }
        }
    createEvent(map)
}

def zwaveEvent(multichannelv3.MultiChannelCmdEncap cmd) {
    log.debug "$cmd"
    def map = [ name: "switch$cmd.destinationEndPoint" ]
        if (cmd.commandClass == 37){
            if (cmd.parameter == [0]) {
                map.value = "off"
            }
            if (cmd.parameter == [255]) {
                map.value = "on"
            }
        }
    createEvent(map)
}

def zwaveEvent(cmd) {
	log.warn "Captured zwave command $cmd"
}

//Commands

def setPoolSetpoint(degreesF) {
	setHeatingSetpoint(degreesF.toDouble())
}

def setPoolSetpoint(Double degreesF) {
	def p = (state.precision == null) ? 1 : state.precision
	delayBetween([
		zwave.thermostatSetpointV1.thermostatSetpointSet(setpointType: 1, scale: 1, precision: p, scaledValue: degreesF).format(),
		zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 1).format()
	])
}

def setSpaSetpoint(degreesF) {
	setSpaSetpoint(degreesF.toDouble())
}

def setSpaSetpoint(Double degreesF) {
	def p = (state.precision == null) ? 1 : state.precision
	delayBetween([
		zwave.thermostatSetpointV1.thermostatSetpointSet(setpointType: 7, scale: 1, precision: p, scaledValue: degreesF).format(),
		zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 7).format()
	])
}

def on() {
	delayBetween([
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: 1, destinationEndPoint: 1, commandClass:37, command:1, parameter:[255]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: 1, destinationEndPoint: 1, commandClass:37, command:2).format()
	], 2300)
}

def off() {
	delayBetween([
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: 1, destinationEndPoint: 1, commandClass:37, command:1, parameter:[0]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: 1, destinationEndPoint: 1, commandClass:37, command:2).format()
	], 2300)
}

//switch instance
def onMulti(value) {
	delayBetween([
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: value, destinationEndPoint: value, commandClass:37, command:1, parameter:[255]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: value, destinationEndPoint: value, commandClass:37, command:2).format()
	], 2300)
}

def offMulti(value) {
	delayBetween([
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: value, destinationEndPoint: value, commandClass:37, command:1, parameter:[0]).format(),
		zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint: value, destinationEndPoint: value, commandClass:37, command:2).format()
	], 2300)
}

//switch1
def on1() {
	onMulti(1)
}

def off1() {
	offMulti(1)
}

//switch2
def on2() {
	onMulti(2)
}

def off2() {
	offMulti(2)
}

//switch3
def on3() {
	onMulti(3)
}

def off3() {
	offMulti(3)
}

//switch4
def on4() {
	onMulti(4)
}

def off4() {
	offMulti(4)
}


def poll() {
    zwave.sensorMultilevelV1.sensorMultilevelGet().format()
}

def refresh() {
	delayBetween([
    zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format(),
    zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:2).format(),
    zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:3, destinationEndPoint:3, commandClass:37, command:2).format(),
    zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:4, destinationEndPoint:4, commandClass:37, command:2).format(),
    zwave.sensorMultilevelV1.sensorMultilevelGet().format(),
    zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 1).format(),
    zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 7).format()
    ], 2500)
}