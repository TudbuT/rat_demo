package de.tudbut.ttd.ratdemo.discord

import de.tudbut.tools.Tools
import tudbut.net.http.HTTPContentType
import tudbut.net.http.HTTPHeader
import tudbut.net.http.HTTPRequest
import tudbut.net.http.HTTPRequestType
import tudbut.obj.TLMap
import tudbut.parsing.JSON
import tudbut.parsing.TCN

import java.nio.charset.StandardCharsets

interface DiscordPayload {

    TCN getContent()

    default byte[] getAttachment() { return [] }

    String getAttachmentName()

    default HTTPRequest createRequest(String webhookPath) {
        String boundary
        String attachmentName = getAttachmentName()
        String attachment = ''
        if (attachmentName != null)
            attachment = new String(getAttachment(), StandardCharsets.ISO_8859_1)
        String content = JSON.write(getContent())
        TLMap<String, String> map = new TLMap<>()
        String random = Tools.randomAlphanumericString(20)
        while (attachment.contains(random) || content.contains(random))
            random = Tools.randomAlphanumericString(20)
        map.set('boundary', boundary = ('---' + random + '-----').toString())
        boundary = '--' + boundary

        StringBuilder body = new StringBuilder()
        body.append(boundary).append('\r\n')
        body.append(new HTTPHeader('Content-Disposition', 'form-data', TLMap.stringToMap('name:payload_json;')).toString()).append('\r\n')
        body.append(new HTTPHeader('Content-Type', HTTPContentType.JSON.asHeaderString).toString()).append('\r\n')
        body.append('\r\n')
        body.append(content).append('\r\n')
        if (attachmentName != null) {
            body.append(boundary).append('\r\n')
            TLMap<String, String> nameMap = new TLMap<>()
            nameMap.set('name', 'file')
            nameMap.set('filename', attachmentName)
            body.append(new HTTPHeader('Content-Disposition', 'form-data', nameMap).toString()).append('\r\n')
            body.append(new HTTPHeader('Content-Type', HTTPContentType.BIN.asHeaderString).toString()).append('\r\n')
            body.append('\r\n')
            body.append(attachment).append('\r\n')
        }
        body.append(boundary).append('--')

        return new HTTPRequest(HTTPRequestType.POST, 'https://discord.com', 443, webhookPath, new HTTPHeader('Content-Type', 'multipart/form-data', map), body.toString())
    }


}