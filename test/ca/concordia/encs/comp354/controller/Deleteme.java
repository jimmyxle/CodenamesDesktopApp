package ca.concordia.encs.comp354.controller;

import org.junit.Test;

import ca.concordia.encs.comp354.model.Team;

public class Deleteme extends AbstractPlayerTest {

    @Test
    public void deleteme() {
        givesAllClues(new SpyMaster(Team.RED, new SequentialSpyMasterStrategy()));
    }
    
}
