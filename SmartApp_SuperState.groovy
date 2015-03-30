/**
 *  superState	1.0
 *
 *  Copyright 2015 Mike Maxwell
 *  
 *  Device (switch/dimmer/color) state capture and replay utility.
 *	- resulting scene is assigned to a child device
 *  - scene devices are editable post capture (recapture/add/delete)
 *
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
definition(
    name: "superState",
    namespace: "mmaxwell",
    author: "Mike Maxwell",
    description: "Device state capture, replay and edit tool",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/Cat-ModeMagic.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/Cat-ModeMagic@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/Cat-ModeMagic@3x.png"
)

preferences {
    page(name: "main")
    page(name: "group",nextPage	: "main")
    page(name: "scene",nextPage	: "main")
    //page(name: "delete",nextPage: "main")
}

def main(){
	def nextGroupIDX = getNextGroupIDX()
    def nextGroupID = "g${nextGroupIDX}"
    dynamicPage(name: "main", title: "superState", uninstall: true,install: true) {
        section("Device groups"){
   	        def prefGroups = getGroupMaps()
            prefGroups.each(){ prefGroup ->
            	 href(
                    name		: prefGroup.key
                    ,title		: prefGroup.value 
                    ,required	: false
                    ,params		: [groupID:prefGroup.key]
                    ,page		: "group"
                    ,description: null
                    ,state		: isGroupComplete(prefGroup.key)
                )
            }
        }
       section(){
      		//always have a link for adding a new group
            href(
            	name		: nextGroupID
            	,title		: "Add a device group..." 
                ,required	: false
                ,params		: [groupID:nextGroupID]
                ,page		: "group"
                ,description: null
            )
        }
    }
}

def group(params){
	def groupID = params.groupID
    def switchID = groupID + "dswitches" 
    def lockID = groupID + "dlocks"
    def relayID = groupID + "drelays"
    def valveID = groupID + "dvalves"
    def doorID = groupID + "ddoors"

	def nextSceneIDX = getNextSceneIDX(groupID)
    def nextSceneID = "${groupID}s${nextSceneIDX}"

   	dynamicPage(name: "group", title: getGroupPageTitle(groupID), uninstall: false,install: false) {
        section() {
            input(
            	name			: groupID
                ,title			: "Group Name"
                ,multiple		: false
                ,required		: true
                ,type			: "text"
            )
         }
        section("Scenes"){
            def prefScenes = getSceneMaps(groupID)
            prefScenes.each(){ prefScene ->
                href(
                	name		: prefScene.key
                	,title		: prefScene.value
                	,required	: false
                    ,params		: [sceneID:prefScene.key]
                    ,page		: "scene"
                    ,state		: "complete"
                    ,description: null
                )
            }
        }
         //need to skip this section if groupID.value is null
         //some bug here if we create a scene before the group is saved
         //log.debug "settings on group page:${settings}"
         if (settings[groupID]){
         	section(){
        		//always have a link for adding a new scene
        	    href(
        	        name		: nextSceneID
        	        ,title		: "Add a Scene..." 
        	        ,required	: false
        	        ,params		: [sceneID:nextSceneID]
        	        ,page		: "scene"
        	        ,description: null
            	)  
        	} 
        }
        section("Devices"){
  			input(
        	    name			: switchID
        	    ,title			: "Switches"
        	    ,multiple		: true
        	    ,required		: false
            	,type			: "capability.switch"
        	)
            /*
  			input(
        	    name			: relayID
        	    ,title			: "Relays"
        	    ,multiple		: true
        	    ,required		: false
            	,type			: "capability.relayswitch"
        	)
   			input(
        	    name			: lockID
        	    ,title			: "Locks"
        	    ,multiple		: true
        	    ,required		: false
            	,type			: "capability.lock"
        	)
   			input(
        	    name			: valveID
        	    ,title			: "Valves"
        	    ,multiple		: true
        	    ,required		: false
            	,type			: "capability.valve"
        	)
   			input(
        	    name			: doorID
        	    ,title			: "Doors"
        	    ,multiple		: true
        	    ,required		: false
            	,type			: "capability.doorcontrol"
        	)
            */
		}
	}
}
def scene(params){
	def sceneID = params.sceneID
	dynamicPage(name: "scene", title: getSceneTitle(sceneID), install: false) {
        section() {
     		input(
        	    name		: sceneID
                ,type		: "text"
        	    ,title		: "Name for this Scene..."
        	    ,required	: true
        	)
        }
    }
}

def installed() {
    state.deviceMaps = [:]
    state.vtMaps = [:]
	//log.debug "Installed with settings: ${settings}"
}

def updated() {
	//log.debug "Updated with settings: ${settings}"
	unsubscribe()
    manageChildDevices()
    def sceneSwitches = getChildDevices()
    subscribe(sceneSwitches,"switch",cmdHandler)
    //remove stale device maps...
	cleanDeviceMaps()    
    log.debug "deviceMaps:${state.deviceMaps}"
    //log.debug "settings:${settings}"
    //log.debug "vtMaps:${state.vtMaps}"
    
}
def cleanDeviceMaps(){
	//loop though device maps, remove those not in in device groups
    def allDeviceIDs  = []
    def groupIDs = getGroupMaps()
    //log.debug "test:${getDeviceKeysFromGroup("g1")}"
    groupIDs.each{ groupID ->
        def deviceKeys = getDeviceKeysFromGroup(groupID.key)
        //log.debug "devicesKeys:${deviceKeys}"
        deviceKeys.each{ deviceKey ->
        	//log.debug "stDevices:${this[deviceKey]}"	
            def stDevices = this[deviceKey]
            stDevices.each{ stDevice ->
            	allDeviceIDs.push(stDevice.id)
                //log.debug "~~~~${stDevice.id}~~~~"
            }
        }
    }
	//log.debug "stDevices:${allDeviceIDs}"
    def toRemove = []
	state.deviceMaps.each{ deviceMap ->
    	//log.debug "device:${deviceMap.key}"	
        if (!allDeviceIDs.contains(deviceMap.key)){
        	//state.deviceMaps.remove(deviceMap.key)
            //log.debug "remove me:${deviceMap.key}"
            toRemove.push(deviceMap.key)
        }
    }
    //log.debug "remove these:${toRemove}"
    toRemove.each{ it ->
    	state.deviceMaps.remove(it)
    }

}

/**************											****************
virtual child device switch to scene mappings
state.vtMaps 	[virtualChildDevice.Id:sceneID]

[2ef9cc26-c9c6-4445-bac9-59457f194f88:g1s2,.....]


scene device to scene and settings mappings
state.deviceMaps [deviceMap,deviceMap,...]
deviceMap [deviceID:[name:deviceName,old:[settingsMap],scenes:[sceneMap,sceneMap,...]]
sceneMap [sceneID:[settingsMap]]
settingsMap [attribute:value,attribute:value,...]

[9b5c043e-ede1-443f-a245-33095e548a4e:[old:[switch:null], scenes:[g1s2:[switch:on], g1s1:[switch:on]], name:Pantry Lights]],...]

***************											***************/
def manageChildDevices(){
	def newVs
    def networkID
    def vTileName
    def vTileLabel
	def deviceID
    def uiGroups = getGroupMaps()
    //need to loop through all groups, then each scene
	uiGroups.each{ group ->
    	def scenes = getSceneMaps(group.key)
        //log.debug "makechild Group:${group.key}"
        //log.debug "makechild scenes:${scenes}"
        scenes.each{ scene ->
    		//log.debug "makechild scene:${scene.key}"
        	if (!state.vtMaps.containsValue(scene.key)) {
        		//log.debug "missing:${scene.key} ${scene.value}"
            	networkID = app.id + "/" + scene.key
          		vTileName = "${group.key}-${scene.key}"
            	vTileLabel = "${group.value}-${scene.value}"
            	newVs = addChildDevice("mmaxwell", "superState switch", networkID, null, [name: "${vTileName}", label: "${vTileLabel}", completedSetup: true])
            	deviceID = newVs.id
            	state.vtMaps << [(deviceID):scene.key]
        	} else {
        		//log.debug "vs exists already"
        	}
        }
    }
    //now go thhrough vtMaps, remove any children not in settings
    //vt:2ef9cc26-c9c6-4445-bac9-59457f194f88=g1s2 should be deleted
    
    state.vtMaps.each{ it ->
    	log.debug "vsm:${it}"
    	if (!settings.containsKey(it.value)){
        	log.debug "vt:${it} should be deleted"
    		//won't let me delete..., says in use???
            //networkID = app.id + "/" + it.value
            //deleteChildDevice(networkID)
        }
    }
 	//log.debug "vtMaps:${state.vtMaps}"
}

def cmdHandler(evt){
    def sceneID = state.vtMaps[evt.deviceId]
    def nid = app.id + "/" + sceneID
    def cmd = evt.data.split("~")[1]
    def logInfo = true
    def stateMapsOK = true
    if (logInfo) log.info "cmdHandler- scene:${sceneID} name:${evt.name} value:${evt.value}  cmd:${cmd}"
    //log.debug "cmdHandler- state.deviceMaps:${state.deviceMaps}"
	
    //events to ignore
	if (cmd in ["warn","override"]) return
	
	def isOverride = false
    def sceneSwitches = getChildDevices()
    def callingSwitch = getChildDevice(nid)
   	//check if other scene switches are activeted
	sceneSwitches.each{ sceneSwitch ->
		//log.debug "cmdHandler - sceneSwitch:${sceneSwitch.name} value:${sceneSwitch.currentValue("switch")} evtName:${evt.deviceId}"  
		if (sceneSwitch.currentValue("switch") != "off" &&  sceneSwitch.id != evt.deviceId){
        	sceneSwitch.overrideScene()
            isOverride = true
            if (logInfo) log.info "cmdHandler- override request from ${evt.displayName} to ${sceneSwitch.displayName}"
        }
    }
    
    if (evt.value == "on" && cmd == "snap"){
    	if (logInfo) log.info "cmdHandler- ${sceneID} snap request"
        sceneSnap(sceneID,true)
        callingSwitch.off()
        return
    }
    
    if (evt.value == "on" && isOverride){
        if (logInfo) log.info "cmdHandler- ${sceneID} on with ignore snap request"
      	stateMapsOK = setScene(sceneID,true,false)
        //return
    } else
    if (evt.value == "on"){
    	if (logInfo) log.info "cmdHandler- ${sceneID} on request"
        sceneSnap(sceneID,false)
        stateMapsOK = setScene(sceneID,true,false)
        //return
    } else
    if (evt.value == "off" && cmd == "restore"){
       	if (logInfo) log.info "cmdHandler- ${sceneID} off with restore request"
        stateMapsOK = setScene(sceneID,false,true)
       	//return
    } else
    if (evt.value == "off"){
       	if (logInfo) log.info "cmdHandler- ${sceneID} off request"
        setScene(sceneID,false,false)
        //return
    } else log.debug "last man standing"

	if (!stateMapsOK){
    	log.warn state.mapsCheck.replace("sceneSwitchName",evt.displayName)
        callingSwitch.warn()
    }
}

def setScene(sceneID,turnOn,restore){
	//if turnOn == false and restore == true, we reset the state to old
    //if turnOn == false and restore == false, then just turn everything off...
    //if turnOn == true, we turn on the scene, restore doesn't matter
    
    log.debug "setScene sceneID:${sceneID} turnOn:${turnOn} restore:${restore}"
    def sceneMap = [:]
    def savedMap
    def deviceMap = [:]
    def deviceKeys = getDeviceKeysFromScene(sceneID) //represents list of devices included in this group
    def hasDevices = deviceKeys.size() != 0
    def success = true
    def missingDevices = []
    state.mapsCheck = ""
    def logInfo = false
	if (hasDevices){
		deviceKeys.each{ deviceKey ->
    		def groupDevices = this[deviceKey] //devices in this group from prefs
        	groupDevices.each{ stDevice ->
        		deviceMap = state.deviceMaps[stDevice.id]
                if (deviceMap != null){
            		sceneMap = deviceMap.scenes[sceneID] 
            		if (sceneMap != null){
						if (restore){
            				savedMap = deviceMap.old
                            //
    						def crntEqualsSaved = true
    						def crntEqualsScene = true
                			//log.debug "${stDevice.displayName}: saved:${savedMap} scene:${sceneMap}"
                			sceneMap.each{ attribute ->
                				def crntValue = stDevice.currentValue(attribute.key).toString()
                    			//log.debug "current- attr:${attribute.key} value:${crntValue}"
                    			//log.debug "attr:${attribute.key} crnt:${crntValue} save:${savedMap[attribute.key].value} scene:${attribute.value}"
                    
                				if (attribute.value.toString() != crntValue){
                    				//log.debug "NEQ scene attr:[${attribute.value}][${crntValue}]"
                    				if (crntEqualsScene){
                        				//log.debug "set false"
                        				crntEqualsScene = false
                        			}
                    			}
                    			//def savedAttribute = savedMap[attribute.key]
                    			if ("${savedMap[attribute.key].value}" != crntValue){
                    				//log.debug "NEQ save attr:[${savedMap[attribute.key].value}][${crntValue}]"
                    				if (crntEqualsSaved){
                        				//log.debug "set false"
                        				crntEqualsSaved = false
                        			}
                    			}
                			}
                			log.debug "${stDevice.displayName}: crntEqualsSaved:${crntEqualsSaved} crntEqualsScene:${crntEqualsScene}" 
						}    
            			if (turnOn){
							// scene on request            
            				if (logInfo) log.info "scene on request for ${sceneID} ${stDevice.displayName}"
							//this device goes on
                			if (sceneMap.switch == "on"){	
                    			//color
								if (sceneMap.color) {
                        			stDevice.on()
                        			def colorMap = [hue:sceneMap.hue.toInteger(),saturation:sceneMap.saturation.toInteger(),level:sceneMap.level]
                    				stDevice.setColor(colorMap)  
                            		log.debug "color:${stDevice.displayName}, ON"
                    			//dimmer
                    			} else if (sceneMap.level){
                      				stDevice.setLevel(sceneMap.level)	
                        			log.debug "dimmer:${stDevice.displayName}, ON"
                    			//switch    
                    			} else {
                    				stDevice.on()
                        			log.debug "switch:${stDevice.displayName}, ON"
                    			}
               				//this device goes off
               				} else {
               					stDevice.off()
                    			log.debug "${stDevice.displayName}, OFF"
               				}
            			} else {
							if (restore){
                				// scene off request with restore
                    			if (logInfo) log.info "scene off request with restore for ${sceneID} ${stDevice.displayName}"
                    				//replace with restore code
                                    stDevice.off()
                                    
                
                				} else {
                					//scene off request
                    				if (logInfo) log.info "scene off request for ${sceneID} ${stDevice.displayName}"
                    				stDevice.off()
                				}
            			}
                    } else {
            		   	log.debug "setScene- sceneMap is null????"
           				success = false
					}
        		} else {
                	//log.debug "setScene- no scene snapped stDevice:${stDevice.displayName}"
                    missingDevices.push(stDevice.displayName)
					success = false
                }
    		}//group device loop
        }//device keys loop
        if (!success) state.mapsCheck = "Device(s):${missingDevices.toListString()} for scene 'sceneSwitchName' have not been snapped, please re-snap the scene."                    
    //no devices        
    } else {
    	log.debug "setScene- no devices"
        state.mapsCheck = "No devices are selected for device group '${getGroupDisplayNameFromScene(sceneID)}', select your devices and snap a scene."                    
        success = false
    }
	return success
}

def sceneSnap(sceneID,isNew) {
	def attributesToSave = ['hue', 'saturation', 'color', 'switch', 'level']
    //def attributesToSave = ['color', 'switch', 'level']
	def allAttributes = []
    def savedMap
    def newMap
    def dataMap
    def deviceMap
    def sceneMap
    def deviceKeys = getDeviceKeysFromScene(sceneID)
    //initial snapshot
	if (isNew){
    	deviceKeys.each{ deviceKey ->
    		//log.debug "deviceKey:${deviceKey}"
    		def groupDevices = this[deviceKey]
        	//log.debug "groupDevices:${groupDevices}"
        	groupDevices.each{ stDevice ->
        		//log.debug "stDevice:${stDevice}"	
        		allAttributes = stDevice.supportedAttributes.collect{it.toString()}
        		newMap = [:]
        		savedMap = [:]
        		deviceMap = [:]
        		dataMap = [:]
        		sceneMap = [:]
        		//check if device has a device map
        		if (!state.deviceMaps.containsKey(stDevice.id)){
        			//log.debug "add device:${stDevice.id} and scene:${sceneID}"
    				allAttributes.each {attribute ->
        				if (attributesToSave.contains(attribute)){
                			newMap << [(attribute):stDevice.currentValue(attribute)]
                			savedMap << [(attribute):null]
        				}
            		}
            		sceneMap = [(sceneID):newMap]
        			dataMap = [name:stDevice.displayName, old:savedMap,scenes:sceneMap]
            		deviceMap = [(stDevice.id):dataMap]
        			state.deviceMaps << deviceMap
        		} else {
            		//does it have this scene?
            		deviceMap = state.deviceMaps[stDevice.id]
            		if (!deviceMap.scenes.containsKey(sceneID)){
            			//log.debug "add scene:${sceneID} to device:${stDevice.id}"
       	 				allAttributes.each {attribute ->
        					if (attributesToSave.contains(attribute)){
                				newMap << [(attribute):stDevice.currentValue(attribute)]
        					}
                		}
                		sceneMap = [(sceneID):newMap]
                		deviceMap.scenes << sceneMap
            		} else {
            			//resnap settings here
            			//log.debug "resnap for device:${stDevice.id} scene:${sceneID}"
                		sceneMap = deviceMap.scenes[sceneID]
                		allAttributes.each {attribute ->
        					if (attributesToSave.contains(attribute)){
                        		sceneMap[attribute] = stDevice.currentValue(attribute)
        					}
            			}
            		}
        		}
			}
  		}
    //snapshot used to save previous state
	} else {
    	//specific device map
    	//9b5c043e-ede1-443f-a245-33095e548a4e:[old:[switch:null], scenes:[g1s2:[switch:on], g1s1:[switch:on]], name:Pantry Lights]] 
    	//save previous state
   		deviceKeys.each{ deviceKey ->
    		def groupDevices = this[deviceKey]
        	groupDevices.each{ stDevice ->
            	//old:[level:null, saturation:null, color:null, hue:null, switch:null]
                deviceMap = state.deviceMaps[stDevice.id]
           		if (deviceMap != null){
                	savedMap = deviceMap.old
                	savedMap.each{ attribute ->
                		deviceMap.old[attribute.key] = stDevice.currentValue(attribute.key) 
                	}
                }
            }
        }
        //log.debug "sceneSnap- ${sceneID} savedMap:${savedMap}"
	}
    //log.debug "sceneSnap ${sceneID} isNew:${isNew}"
    //log.debug "state.deviceMaps:${state.deviceMaps}"
}	

/**********************************************
UI preference settings methods
***********************************************/
def isGroupComplete(groupID){
	def boolean hasScenes = getSceneMaps(groupID) ?: false
    def boolean hasDevices = getDeviceLists(groupID) ?: false
    if (hasScenes && hasDevices) return "complete"
    else return "incomplete"
}

def getDeviceKeysFromGroup(groupID){
	return settings.findAll(){it.key ==~ /${groupID}d[a-z,A-Z]+/}.keySet()
}

def getDeviceLists(groupID){
	// [g1:Master, g1s1:some Scene, g1dswitches:[AeonD11, Bar Strip]]
    //all device lists gxdxxxx
    return settings.findAll(){it.key ==~ /${groupID}d[a-z,A-Z]+/}
}
def getDeviceKeysFromScene(sceneID){
	//get group from scene
    def groupID = sceneID.replaceFirst(/s[0-9]+/,"")
    return settings.findAll(){it.key ==~ /${groupID}d[a-z,A-Z]+/}.keySet()
}

def getGroupDisplayNameFromScene(sceneID){
	def groupID = sceneID.replaceFirst(/s[0-9]+/,"")
    return settings[groupID]
}
def getSceneDisplayNameFromScene(sceneID){
	return settings[sceneID]
}

def getGroupPageTitle(groupID){
    return settings[groupID] ?: "Device Group ${groupID.replace("g","")}"
}

def getSceneTitle(sceneID){
	return settings[sceneID] ?: sceneID.replace("g","Group ").replace("s",", Scene ") 
}

def getGroupMaps(){
	return settings.findAll(){it.key ==~ /g[0-9]+/}.sort{it.key}
}

def getSceneMaps(groupID){
	//scene key format : g1s1
    return settings.findAll(){it.key ==~ /${groupID}s[0-9]+/}.sort{it.key}
}

def getNextGroupIDX(){
	def found = settings.findAll(){it.key ==~ /g[0-9]+/}
	def next = 0
	def crnt
	found.each(){ it.key
	    crnt = it.key.replace("g","").toInteger()
	    if (crnt > next ) next = crnt
	}
	next ++
	return next
}

def getNextSceneIDX(groupID){
	def found = settings.findAll(){it.key ==~ /${groupID}s[0-9]+/}
	def next = 0
	def crnt
	found.each(){ it.key
    	crnt = it.key.replace("${groupID}s","").toInteger()
     	if (crnt > next ) next = crnt
	}
	next ++
	return next
}
