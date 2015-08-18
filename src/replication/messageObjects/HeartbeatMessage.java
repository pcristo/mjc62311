package replication.messageObjects;

import java.io.Serializable;

public class HeartbeatMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	public HeartbeatMessage(int port) {
            this.port = port;
    }

    public Integer getPort() {
            return port;
    }

    private Integer port;
    /*private String msg = "This was a triumph!\n" +
            "I'm making a note here:\n" +
            "Huge success!\n" +
            "\n" +
            "It's hard to overstate\n" +
            "my satisfaction.\n" +
            "\n" +
            "Aperture Science:\n" +
            "We do what we must\n" +
            "because we can\n" +
            "For the good of all of us.\n" +
            "Except the ones who are dead.\n" +
            "\n" +
            "But there's no sense crying\n" +
            "over every mistake.\n" +
            "You just keep on trying\n" +
            "'til you run out of cake.\n" +
            "And the science gets done.\n" +
            "And you make a neat gun\n" +
            "for the people who are\n" +
            "still alive.\n" +
            "\n" +
            "I'm not even angry...\n" +
            "I'm being so sincere right now.\n" +
            "Even though you broke my heart,\n" +
            "and killed me.\n" +
            "\n" +
            "And tore me to pieces.\n" +
            "And threw every piece into a fire.\n" +
            "As they burned it hurt because\n" +
            "I was so happy for you!\n" +
            "\n" +
            "Now, these points of data\n" +
            "make a beautiful line.\n" +
            "And we're out of beta.\n" +
            "We're releasing on time!\n" +
            "So I'm GLaD I got burned!\n" +
            "Think of all the things we learned!\n" +
            "for the people who are\n" +
            "still alive.\n" +
            "\n" +
            "Go ahead and leave me...\n" +
            "I think I'd prefer to stay inside...\n" +
            "Maybe you'll find someone else\n" +
            "to help you.\n" +
            "Maybe Black Mesa?\n" +
            "That was a joke. Ha Ha. Fat Chance!\n" +
            "\n" +
            "Anyway this cake is great!\n" +
            "It's so delicious and moist!\n" +
            "\n" +
            "Look at me: still talking\n" +
            "when there's science to do!\n" +
            "When I look out there,\n" +
            "it makes me glad I'm not you.\n" +
            "\n" +
            "I've experiments to run.\n" +
            "There is research to be done.\n" +
            "On the people who are\n" +
            "still alive.\n" +
            "And believe me I am\n" +
            "still alive.\n" +
            "I'm doing science and I'm\n" +
            "still alive.\n" +
            "I feel fantastic and I'm\n" +
            "still alive.\n" +
            "While you're dying I'll be\n" +
            "still alive.\n" +
            "And when you're dead I will be\n" +
            "still alive\n" +
            "\n" +
            "Still alive.\n" +
            "\n" +
            "Still alive.";*/

}
