definition(
    name: "Battery Checker",
    namespace: "rocktavious",
    author: "rocktavious",
    description: "Send messages for low battery levels of selected devices.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
)

preferences {
    section("Configuration") {
	    input "alarmAt", "number", title: "Alert when below percent", required: true
        input "batteryDevices", "capability.battery", title: "Which devices?", multiple: true
        input "sendPush", "bool", required: false, title: "Send Push Notification (optional)"
        input("recipients", "contact", title: "Send notifications to") {
            input "phone", "phone", title: "Send SMS Notification (optional)",
                description: "Phone Number", required: false
        }
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unschedule()
    initialize()
}

def initialize() {
    //schedule the job
    schedule("0 0 10am 1,15 * ?", doBatteryCheck)
}

def doMessageSending(message) {
    if (sendPush) {
        sendPush(message)
    }
    if (location.contactBookEnabled && recipients) {
        sendNotificationToContacts(message, recipients)
    } else {
        if (phone) {
            sendSms(phone, message)
        }
    }
}

def doBatteryCheck() {
    def belowLevelCntr = 0
    def message = ""
    for (batteryDevice in batteryDevices) {
    	def batteryLevel = batteryDevice.currentValue("battery")
        message += "${batteryDevice.name} named ${batteryDevice.label} is at: ${batteryLevel}% \n"
        if ( batteryLevel <= settings.alarmAt.toInteger() ) {
            belowLevelCntr++
        }
    }
    if ( belowLevelCntr ){
    	message = "You have ${belowLevelCntr} devices below the set alarm level. \n" + message
    } else {
        message = "Battery Check App executed with no devices below alarm level"
    }
	doMessageSending(message)
}