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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class IceMaker extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.ICEMAKER;
		usesTargeting = true;
		talentChance = 1/(float) Recipe.OUT_QUANTITY;
	}
	
	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		int cell = bolt.collisionPos;

		GameScene.add(Blob.seed(cell, 5, Freezing.class));
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round((50 + 40) * (quantity/(float) Recipe.OUT_QUANTITY));
	}

	@Override
	public int energyVal() {
		return (int)(16 * (quantity/(float) Recipe.OUT_QUANTITY));
	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		private static final int OUT_QUANTITY = 8;
		
		{
			inputs =  new Class[]{PotionOfSnapFreeze.class};
			inQuantity = new int[]{1};
			
			cost = 6;
			
			output = IceMaker.class;
			outQuantity = 8;
		}
		
	}
}
