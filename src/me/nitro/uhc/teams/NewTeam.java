package me.nitro.uhc.teams;

import java.util.HashSet;
import java.util.UUID;

public class NewTeam {
    UUID leaderUUID;
    HashSet<UUID> members;
    HashSet<UUID> invitees;

    public NewTeam(UUID leaderUUID) {
        this.leaderUUID = leaderUUID;
        members = new HashSet<>();
        invitees = new HashSet<>();
        members.add(leaderUUID);
    }

    public HashSet<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID player) {
        members.add(player);
    }

    public boolean removePlayer(UUID player) {
        if (members.contains(player)) {
            members.remove(player);
            return true;
        } else {
            return false;
        }
    }



}
