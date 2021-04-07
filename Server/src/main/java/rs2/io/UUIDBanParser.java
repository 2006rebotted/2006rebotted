package rs2.io;

import rs2.game.model.World;
import rs2.util.TextFileParser;

import java.io.IOException;
import java.util.Scanner;

public class UUIDBanParser extends TextFileParser {

    public UUIDBanParser() {
        super("./data/punishment/uuid_bans");
    }

    @Override
    public void parse(Scanner reader) throws IOException {
        String uuid = reader.nextLine();
        World.world.getBannedUUIDs().add(uuid);
    }

}
