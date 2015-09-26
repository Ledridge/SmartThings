/**
 *  Foscam Universal Device
 *
 *  Copyright 2014 skp19
 *
 */
metadata {
	definition (name: "WiFi 370 LED Strip Controller", namespace: "Ledridge", author: "Ledridge") {
		capability "Switch Level"
		capability "Actuator"
		capability "Color Control"
		capability "Switch"
		capability "Refresh"
		capability "Sensor"
        
		command "setAdjustedColor"
		command "refresh"
	}
    
preferences {
	input("ip", "string", title:"Controller IP Address", description: "Controller IP Address", defaultValue: "192.168.1.69", required: true, displayDuringSetup: true)
	input("port", "string", title:"Controller Port", description: "Controller Port", defaultValue: 5577 , required: true, displayDuringSetup: true)
	input("username", "string", title:"Controller Username", description: "Controller Username", defaultValue: admin, required: true, displayDuringSetup: true)
	input("password", "password", title:"Controller Password", description: "Controller Password", defaultValue: nimda, required: true, displayDuringSetup: true)
	}

	standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
		state "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821"
		state "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff"
	}
	standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
		state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
	}
	controlTile("rgbSelector", "device.color", "color", height: 3, width: 3, inactiveLabel: false) {
		state "color", action:"setAdjustedColor"
	}
	controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 2, inactiveLabel: false, range:"(0..100)") {
		state "level", action:"switch level.setLevel"
	}
	valueTile("level", "device.level", inactiveLabel: false, decoration: "flat") {
		state "level", label: 'Level ${currentValue}%'
	}
	controlTile("saturationSliderControl", "device.saturation", "slider", height: 1, width: 2, inactiveLabel: false) {
		state "saturation", action:"color control.setSaturation"
	}
	valueTile("saturation", "device.saturation", inactiveLabel: false, decoration: "flat") {
		state "saturation", label: 'Sat ${currentValue}    '
	}
	controlTile("hueSliderControl", "device.hue", "slider", height: 1, width: 2, inactiveLabel: false) {
		state "hue", action:"color control.setHue"
	}
	valueTile("hue", "device.hue", inactiveLabel: false, decoration: "flat") {
		state "hue", label: 'Hue ${currentValue}   '
	}

	main(["switch"])
	details(["switch", "levelSliderControl", "rgbSelector", "refresh"])

}

// parse events into attributes
def parse(description) {
	log.debug "parse() - $description"
	def results = []
	def map = description
	if (description instanceof String)  {
		log.debug "Hue Bulb stringToMap - ${map}"
		map = stringToMap(description)
	}
	if (map?.name && map?.value) {
		results << createEvent(name: "${map?.name}", value: "${map?.value}")
	}
	results
}

// handle commands
def on() {
	parent.on(this)
	sendEvent(name: "switch", value: "on")
}

def off() {
	parent.off(this)
	sendEvent(name: "switch", value: "off")
}

def nextLevel() {
	def level = device.latestValue("level") as Integer ?: 0
	if (level <= 100) {
		level = Math.min(25 * (Math.round(level / 25) + 1), 100) as Integer
	}
	else {
		level = 25
	}
	setLevel(level)
}

def setLevel(percent) {
	log.debug "Executing 'setLevel'"
	parent.setLevel(this, percent)
	sendEvent(name: "level", value: percent)
}

def setSaturation(percent) {
	log.debug "Executing 'setSaturation'"
	parent.setSaturation(this, percent)
	sendEvent(name: "saturation", value: percent)
}

def setHue(percent) {
	log.debug "Executing 'setHue'"
	parent.setHue(this, percent)
	sendEvent(name: "hue", value: percent)
}

def setColor(value) {
	log.debug "setColor: ${value}, $this"
	parent.setColor(this, value)
	if (value.hue) { sendEvent(name: "hue", value: value.hue)}
	if (value.saturation) { sendEvent(name: "saturation", value: value.saturation)}
	if (value.hex) { sendEvent(name: "color", value: value.hex)}
	if (value.level) { sendEvent(name: "level", value: value.level)}
	if (value.switch) { sendEvent(name: "switch", value: value.switch)}
}

def setAdjustedColor(value) {
	if (value) {
        log.debug "setAdjustedColor: ${value}"
        def adjusted = value + [:]
        adjusted.hue = adjustOutgoingHue(value.hue)
        // Needed because color picker always sends 100
        adjusted.level = null 
        setColor(adjusted)
    }
}

def refresh() {
	log.debug "Executing 'refresh'"
	parent.poll()
}

def adjustOutgoingHue(percent) {
	def adjusted = percent
	if (percent > 31) {
		if (percent < 63.0) {
			adjusted = percent + (7 * (percent -30 ) / 32)
		}
		else if (percent < 73.0) {
			adjusted = 69 + (5 * (percent - 62) / 10)
		}
		else {
			adjusted = percent + (2 * (100 - percent) / 28)
		}
	}
	log.info "percent: $percent, adjusted: $adjusted"
	adjusted
}



//TAKE PICTURE
def take() {
	log.debug("Taking Photo")
	sendEvent(name: "hubactionMode", value: "s3");
    if(hdcamera == "true") {
		hubGet("cmd=snapPicture2")
    }
    else {
    	hubGet("/snapshot.cgi?")
    }
}
//END TAKE PICTURE

//ALARM ACTIONS
def toggleAlarm() {
	log.debug "Toggling Alarm"
	if(device.currentValue("alarmStatus") == "on") {
    	alarmOff()
  	}
	else {
    	alarmOn()
	}
}

def alarmOn() {
	log.debug "Enabling Alarm"
    sendEvent(name: "alarmStatus", value: "on");
    if(hdcamera == "true") {
		hubGet("cmd=setMotionDetectConfig&isEnable=1")
    }
    else {
    	hubGet("/set_alarm.cgi?motion_armed=1&")
    }
}

def alarmOff() {
	log.debug "Disabling Alarm"
    sendEvent(name: "alarmStatus", value: "off");
    if(hdcamera == "true") {
		hubGet("cmd=setMotionDetectConfig&isEnable=0")
    }
    else {
    	hubGet("/set_alarm.cgi?motion_armed=0&")
    }
}
//END ALARM ACTIONS

//LED ACTIONS
//Toggle LED's
def toggleLED() {
  log.debug("Toggle LED")

  if(device.currentValue("ledStatus") == "auto") {
    ledOn()
  }

  else if(device.currentValue("ledStatus") == "on") {
    ledOff()
  }
  
  else {
    ledAuto()
  }
}

def ledOn() {
    log.debug("LED changed to: on")
    sendEvent(name: "ledStatus", value: "on");
    if(hdcamera == "true") {
	    delayBetween([hubGet("cmd=setInfraLedConfig&mode=1"), hubGet("cmd=openInfraLed")])
    }
    else {
    	hubGet("/decoder_control.cgi?command=95&")
    }
}

def ledOff() {
    log.debug("LED changed to: off")
    sendEvent(name: "ledStatus", value: "off");
    if(hdcamera == "true") {
    	delayBetween([hubGet("cmd=setInfraLedConfig&mode=1"), hubGet("cmd=closeInfraLed")])
    }
    else {
    	hubGet("/decoder_control.cgi?command=94&")
    }
}

def ledAuto() {
    log.debug("LED changed to: auto")
    sendEvent(name: "ledStatus", value: "auto");
	if(hdcamera == "true") {
		hubGet("cmd=setInfraLedConfig&mode=0")
    }
    else {
    	hubGet("/decoder_control.cgi?command=95&")
    }
}
//END LED ACTIONS

//PRESET ACTIONS
def preset1() {
	log.debug("Preset 1 Selected - ${preset1}")
	if(hdcamera == "true") {
		hubGet("cmd=ptzGotoPresetPoint&name=${preset1}")
    }
    else {
    	hubGet("/decoder_control.cgi?command=31&")
    }
}

def preset2() {
	log.debug("Preset 2 Selected - ${preset2}")
	if(hdcamera == "true") {
		hubGet("cmd=ptzGotoPresetPoint&name=${preset2}")
    }
    else {
    	hubGet("/decoder_control.cgi?command=33&")
    }
}

def preset3() {
	log.debug("Preset 3 Selected - ${preset3}")
	if(hdcamera == "true") {
		hubGet("cmd=ptzGotoPresetPoint&name=${preset3}")
    }
    else {
    	hubGet("/decoder_control.cgi?command=35&")
    }
}
//END PRESET ACTIONS

//CRUISE ACTIONS
def cruisemap1() {
	log.debug("Cruise Map 1 Selected - ${cruisemap1}")
	if(hdcamera == "true") {
		hubGet("cmd=ptzStartCruise&mapName=${cruisemap1}")
    }
    else {
    	hubGet("/decoder_control.cgi?command=28&")
    }
}

def cruisemap2() {
	log.debug("Cruise Map 2 Selected - ${cruisemap2}")
	if(hdcamera == "true") {
		hubGet("cmd=ptzStartCruise&mapName=${cruisemap2}")
    }
    else {
    	hubGet("/decoder_control.cgi?command=26&")
    }
}

def stopCruise() {
	log.debug("Stop Cruise")
	if(hdcamera == "true") {
		hubGet("cmd=ptzStopRun")
    }
    else {
    	delayBetween([hubGet("/decoder_control.cgi?command=29&"), hubGet("/decoder_control.cgi?command=27&")])
    }
}
//END CRUISE ACTIONS

//PTZ CONTROLS
def left() {
	if(hdcamera == "true") {
		delayBetween([hubGet("cmd=ptzMoveLeft"), hubGet("cmd=ptzStopRun")])
    }
    else {
    	if(mirror == "true") {
	    	hubGet("/decoder_control.cgi?command=4&onestep=1&")
        }
        else {
        	hubGet("/decoder_control.cgi?command=6&onestep=1&")
        }
    }
}

def right() {
	if(hdcamera == "true") {
		delayBetween([hubGet("cmd=ptzMoveRight"), hubGet("cmd=ptzStopRun")])
    }
    else {
    	if(mirror == "true") {
	    	hubGet("/decoder_control.cgi?command=6&onestep=1&")
        }
        else {
        	hubGet("/decoder_control.cgi?command=4&onestep=1&")
        }
    }
}

def up() {
	if(hdcamera == "true") {
        delayBetween([hubGet("cmd=ptzMoveUp"), hubGet("cmd=ptzStopRun")])
    }
    else {
    	if(flip == "true") {
	    	hubGet("/decoder_control.cgi?command=2&onestep=1&")
        }
        else {
        	hubGet("/decoder_control.cgi?command=0&onestep=1&")
        }
    }
}

def down() {
	if(hdcamera == "true") {
        delayBetween([hubGet("cmd=ptzMoveDown"), hubGet("cmd=ptzStopRun")])
    }
    else {
    	if(flip == "true") {
    		hubGet("/decoder_control.cgi?command=0&onestep=1&")
        }
        else {
        	hubGet("/decoder_control.cgi?command=2&onestep=1&")
        }
    }
}
//END PTZ CONTROLS

def poll() {

	sendEvent(name: "hubactionMode", value: "local");
    //Poll Motion Alarm Status and IR LED Mode
    if(hdcamera == "true") {
		delayBetween([hubGet("cmd=getMotionDetectConfig"), hubGet("cmd=getInfraLedConfig")])
    }
    else {
    	hubGet("/get_params.cgi?")
    }
}

private getLogin() {
	if(hdcamera == "true") {
    	return "usr=${username}&pwd=${password}&"
    }
    else {
    	return "user=${username}&pwd=${password}"
    }
}

private hubGet(def apiCommand) {
	//Setting Network Device Id
    def iphex = convertIPtoHex(ip)
    def porthex = convertPortToHex(port)
    device.deviceNetworkId = "$iphex:$porthex"
    log.debug "Device Network Id set to ${iphex}:${porthex}"

	log.debug("Executing hubaction on " + getHostAddress())
    def uri = ""
    if(hdcamera == "true") {
    	uri = "/cgi-bin/CGIProxy.fcgi?" + getLogin() + apiCommand
	}
    else {
    	uri = apiCommand + getLogin()
    }
    log.debug uri
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
        path: uri,
        headers: [HOST:getHostAddress()]
    )
    if(device.currentValue("hubactionMode") == "s3") {
        hubAction.options = [outputMsgToS3:true]
        sendEvent(name: "hubactionMode", value: "local");
    }
	hubAction
}

//Parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
    
    def map = [:]
    def retResult = []
    def descMap = parseDescriptionAsMap(description)
        
    //Image
	if (descMap["bucket"] && descMap["key"]) {
		putImageInS3(descMap)
	}

	//Status Polling
    else if (descMap["headers"] && descMap["body"]) {
        def body = new String(descMap["body"].decodeBase64())
        if(hdcamera == "true") {
            def langs = new XmlSlurper().parseText(body)

            def motionAlarm = "$langs.isEnable"
            def ledMode = "$langs.mode"

            //Get Motion Alarm Status
            if(motionAlarm == "0") {
                log.info("Polled: Alarm Off")
                sendEvent(name: "alarmStatus", value: "off");
            }
            else if(motionAlarm == "1") {
                log.info("Polled: Alarm On")
                sendEvent(name: "alarmStatus", value: "on");
            }

            //Get IR LED Mode
            if(ledMode == "0") {
                log.info("Polled: LED Mode Auto")
                sendEvent(name: "ledStatus", value: "auto")
            }
            else if(ledMode == "1") {
                log.info("Polled: LED Mode Manual")
                sendEvent(name: "ledStatus", value: "manual")
            }
    	}
        else {
        	if(body.find("alarm_motion_armed=0")) {
				log.info("Polled: Alarm Off")
                sendEvent(name: "alarmStatus", value: "off")
            }
        	else if(body.find("alarm_motion_armed=1")) {
				log.info("Polled: Alarm On")
                sendEvent(name: "alarmStatus", value: "on")
            }
            //The API does not provide a way to poll for LED status on 8xxx series at the moment
        }
	}
}

def parseDescriptionAsMap(description) {
	description.split(",").inject([:]) { map, param ->
		def nameAndValue = param.split(":")
		map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}
}

def putImageInS3(map) {

	def s3ObjectContent

	try {
		def imageBytes = getS3Object(map.bucket, map.key + ".jpg")

		if(imageBytes)
		{
			s3ObjectContent = imageBytes.getObjectContent()
			def bytes = new ByteArrayInputStream(s3ObjectContent.bytes)
			storeImage(getPictureName(), bytes)
		}
	}
	catch(Exception e) {
		log.error e
	}
	finally {
		//Explicitly close the stream
		if (s3ObjectContent) { s3ObjectContent.close() }
	}
}

private getPictureName() {
  def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
  "image" + "_$pictureUuid" + ".jpg"
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