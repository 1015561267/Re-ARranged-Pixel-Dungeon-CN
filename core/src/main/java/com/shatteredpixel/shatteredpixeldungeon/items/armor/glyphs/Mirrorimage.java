/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Mirrorimage extends Armor.Glyph {

    private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xE0AD2F );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = Math.max( 0, armor.buffedLvl() );

        // lvl 0 - 20%
        // lvl 1 - 33%
        // lvl 2 - 43%
        float procChance = (1+level)/(float)(5+level) * procChanceMultiplier(defender);
        float powerMulti = Math.max(1f, procChance);

        if (Random.Float() < procChance && defender == Dungeon.hero){
            ScrollOfMirrorImage.spawnImages(Dungeon.hero, (int)powerMulti);
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }

}
