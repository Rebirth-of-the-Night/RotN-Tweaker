package com.rebirthofthenight.rotntweaker.content.potions;

public class PotionStrongHeart extends PotionBase {

    public PotionStrongHeart() {
        super("strong_heart", true, 0xFF6969);
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

}
