package astraeus.io;

import astraeus.game.model.World;
import astraeus.util.TextFileParser;

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
