package com.slb.file.api;

import com.jcraft.jsch.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SftpTest {

    //@Test
    @Ignore
    public void put_to_sftp() throws JSchException, SftpException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();
        String localFile = "src/main/resources/sample.txt";
        String remoteDir = "remote_sftp_test/";
        channelSftp.put(localFile, remoteDir + "jschFile.txt");
        channelSftp.exit();
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        jsch.setKnownHosts("localhost");
        String username = "Samir-PC";
        String remoteHost = "localhost";
        Session jschSession = jsch.getSession(username, remoteHost);
        byte[] password = "admin".getBytes();
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    //@Test
    /*public void whenDownloadFileUsingJsch_thenSuccess() throws JSchException, SftpException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();

        String remoteFile = "welcome.txt";
        String localDir = "src/main/resources/";

        channelSftp.get(remoteFile, localDir + "jschFile.txt");

        channelSftp.exit();
    }*/


    /*@Test
    public void whenUploadFileUsingSshj_thenSuccess() throws IOException {
        SSHClient sshClient = setupSshj();
        SFTPClient sftpClient = sshClient.newSFTPClient();
        String localFile = null;
        String remoteDir = null;
        sftpClient.put(localFile, remoteDir + "sshjFile.txt");
        sftpClient.close();
        sshClient.disconnect();
    }*/


    /*public SSHClient setupSshj() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        String remoteHost = null;
        client.connect(remoteHost);
        String username = null;
        char[] password = new char[0];
        client.authPassword(username, password);
        return client;
    }*/
}
