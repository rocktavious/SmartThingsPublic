/**
 *  Switch Scheduler
 *
 *  Copyright 2015 The Rockmans
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
    name: "Switch Scheduler",
    namespace: "rocktavious",
    author: "Emily Rockman",
    description: "Turn connected switches on and off at a scheduled time.",
    category: "Convenience",
    iconUrl: "http://cliparts.co/cliparts/8c6/LGn/8c6LGnzcE.png",
    iconX2Url: "http://cliparts.co/cliparts/8c6/LGn/8c6LGnzcE.png",
    iconX3Url: "http://cliparts.co/cliparts/8c6/LGn/8c6LGnzcE.png")


preferences {
	section {
        input "switches", "capability.switch", multiple: true
        input "timeOn", "time", title: "Time On"
        input "timeOff", "time", title: "Time Off"
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    schedule(timeOn, lightsOn)
    schedule(timeOff, lightsOff)
}

def lightsOn() {
    switches.on()
}

def lightsOff() {
    switches.off()
}