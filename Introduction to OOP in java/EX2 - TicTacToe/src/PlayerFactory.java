/**
 * Player factory creates the player types - receiving an input from the user, the player factory then directs
 * to create the type of player necessary.
 * We have four types of players - "human" is the user, "whatever" is the random player, "clever" puts a mark in the first
 * free spot, "snartypamts"
 */
public class PlayerFactory {


    public Player buildPlayer(String name) {
        switch (name) {
            case "human":
                return new HumanPlayer();
            case "whatever":
                return new WhateverPlayer();
            case "clever":
                return new CleverPlayer();
            case "snartypamts":
                return new SnartypamtsPlayer();
            default:
                return null;
        }
    }
}