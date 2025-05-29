/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.seedfinder.SeedALRLogScene;
import com.shatteredpixel.shatteredpixeldungeon.seedfinder.SeedFindLogScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.DeviceCompat;

public class SeedFinderScene extends PixelScene {
	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchScene(TitleScene.class);
	}
	@Override
	public void create() {

		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
				new float[]{1, 1},
				true);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

		Image title = BannerSprites.get( BannerSprites.Type.TITLE_PORT );
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.TITLE_GLOW_PORT ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );

		final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

		StyledButton btnScout = new StyledButton(GREY_TR, Messages.get(SeedFinderScene.class, "scout_seed_button")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade(SeedFindLogScene.class);
			}
		};
		btnScout.icon(Icons.get(Icons.ENTER));
		add(btnScout);

		StyledButton btnSeedfinder = new StyledButton(GREY_TR, Messages.get(this, "seedfinder_button")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade(SeedALRLogScene.class);
			}
		};
		btnSeedfinder.icon(Icons.get(Icons.MAGNIFY));
		add(btnSeedfinder);

		StyledButton btnWndSetings = new StyledButton(GREY_TR, Messages.get(this, "scout_wns")) {
			@Override
			protected void onClick() {
				WndSettings.last_index = 6;
				ShatteredPixelDungeon.scene().add(new WndSettings());
			}
		};
		btnWndSetings.icon(Icons.get(Icons.ENTER));
		add(btnWndSetings);

		StyledButton btnCatalog = new StyledButton(GREY_TR, Messages.get(this, "item_catalog")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade(EXJournalScene.class);
			}
		};
		btnCatalog.icon(new ItemSprite(ItemSpriteSheet.POTION_AZURE));
		add(btnCatalog);

		StyledButton btnSource = new StyledButton(GREY_TR, Messages.get(this, "source")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront( new WndTitledMessage(
						new ItemSprite(ItemSpriteSheet.SR_T5),
						Messages.get(SeedFinderScene.class, "thanks"),
						Messages.get(SeedFinderScene.class, "thanks_info"))
				);
            }
		};
		btnSource.icon(Icons.get(Icons.NEWS));
		add(btnSource);

		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		if (landscape()) {
			btnScout.setRect(title.x-50, topRegion+GAP, ((title.width()+100)/2)-1, BTN_HEIGHT);
			align(btnScout);
			btnSeedfinder.setRect(btnScout.right()+2, btnScout.top(), btnScout.width(), BTN_HEIGHT);
			btnWndSetings.setRect(btnScout.left(), btnScout.bottom()+ GAP, btnScout.width(), BTN_HEIGHT);
			btnCatalog.setRect(btnWndSetings.left(), btnWndSetings.bottom()+GAP, btnWndSetings.width()*2+2, BTN_HEIGHT);
			btnSource.setRect(btnWndSetings.right()+2, btnWndSetings.top(), btnWndSetings.width(), BTN_HEIGHT);
		} else {
			btnScout.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnScout);
			btnSeedfinder.setRect(btnScout.left(), btnScout.bottom()+ GAP, btnScout.width(), BTN_HEIGHT);
			btnWndSetings.setRect(btnScout.left(), btnSeedfinder.bottom()+ GAP, (btnScout.width()/2)-1, BTN_HEIGHT);
			btnCatalog.setRect(btnWndSetings.right()+2, btnWndSetings.top(), btnWndSetings.width(), BTN_HEIGHT);
			btnSource.setRect(btnWndSetings.left(), btnWndSetings.bottom()+ GAP, btnWndSetings.width()*2+2, BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

		fadeIn();
	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton(Chrome.Type type, String label){
			super(type, label);
			icon(new Image(Icons.get(Icons.PREFS)));
		}

		@Override
		public void update() {
			super.update();
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.scene().add(new WndSettings());
		}
	}

}
