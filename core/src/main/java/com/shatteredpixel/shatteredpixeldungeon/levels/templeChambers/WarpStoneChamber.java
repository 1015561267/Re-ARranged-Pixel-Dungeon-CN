package com.shatteredpixel.shatteredpixeldungeon.levels.templeChambers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;

public class WarpStoneChamber extends Chamber {
    {
        isBuildWithStructure = true;
    }

    @Override
    public int[] roomStructure() {
        return new int[] {
                //this is made by sandbox pd
                25, 26, 25, 25, 11, 25, 25, 26, 14, 25, 25, 25, 25, 25, 25, 25, 25, 25, 0, 0, 0, 0, 0, 0, 0, 11, 25, 0, 0, 26, 0, 0, 0, 26, 25, 0, 0, 0, 25, 25, 25, 25, 14, 25, 0, 0, 0, 11, 0, 0, 25, 25, 0, 11, 0, 0, 0, 25, 14, 25, 25, 0, 0, 0, 0, 0, 0, 25, 25, 26, 0, 0, 25, 11, 25, 26, 0, 25, 0, 0, 25, 0, 25, 0, 11, 25, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 11, 0, 25, 0, 25, 25, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 25, 25, 25, 0, 25, 25, 25, 25, 25, 25, 25, 25, 25, 14, 25, 0, 0, 26, 14, 25, 0, 25, 14, 11, 14, 25, 0, 0, 0, 14, 11, 14, 0, 0, 0, 25, 14, 11, 14, 26, 0, 25, 14, 26, 0, 0, 25, 14, 25, 25, 25, 25, 25, 25, 25, 25, 25, 0, 25, 25, 25, 0, 0, 25, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 0, 25, 0, 11, 0, 0, 25, 0, 0, 0, 0, 0, 0, 0, 0, 25, 11, 0, 25, 0, 25, 0, 0, 25, 0, 26, 25, 11, 25, 0, 0, 26, 25, 25, 0, 0, 0, 0, 0, 0, 25, 25, 14, 25, 0, 0, 0, 11, 0, 25, 25, 0, 0, 11, 0, 0, 0, 25, 14, 25, 25, 25, 25, 0, 0, 0, 25, 26, 0, 0, 0, 26, 0, 0, 25, 11, 0, 0, 0, 0, 0, 0, 0, 25, 25, 25, 25, 25, 25, 25, 25, 25, 14, 26, 25, 25, 11, 25, 25, 26, 25
        };
    }

    @Override
    public void build() {
        super.build();

        int[][] offsets = {
                {-3, -4}, {-6, -5}, {-4, -8},
                {4, -3}, {5, -6}, {8, -4},
                {3, 4}, {6, 5}, {4, 8},
                {-4, 3}, {-5, 6}, {-8, 4},
                {0, 0}, {0, -7}, {7, 0}, {0, 7}, {-7, 0},
        };

        for (int pos : customOffsetArray(offsets)) {
            level.drop(new StoneOfBlink(), pos);
        }
    }
}