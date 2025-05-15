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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.AvailableUpdateData;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndVictoryCongrats;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;

import java.util.Date;

public class TitleScene extends PixelScene {
	
	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
				new float[]{1, 1},
				false);

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( landscape() ? BannerSprites.Type.TITLE_LAND : BannerSprites.Type.TITLE_PORT);
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		if (landscape()){
			placeTorch(title.x + 16, title.y + 70);
			placeTorch(title.x + title.width - 16, title.y + 70);
		} else {
			placeTorch(title.x + 16, title.y + 70);
			placeTorch(title.x + title.width - 16, title.y + 70);
		}

		Image signs = new Image(BannerSprites.get( landscape() ? BannerSprites.Type.TITLE_GLOW_LAND : BannerSprites.Type.TITLE_GLOW_PORT)){
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
		
		StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().size() == 0){
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
				} else {
					ShatteredPixelDungeon.switchNoFade( StartScene.class );
				}
			}
			
			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (DeviceCompat.isDebug()) {
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
					return true;
				}
				return super.onLongClick();
			}
		};
		btnPlay.icon(Icons.get(Icons.ENTER));
		add(btnPlay);

		StyledButton btnSupport = new SupportButton(GREY_TR, Messages.get(this, "support"));
		add(btnSupport);

		StyledButton btnRankings = new StyledButton(GREY_TR,Messages.get(this, "rankings")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(Icons.get(Icons.RANKINGS));
		add(btnRankings);
		Dungeon.daily = Dungeon.dailyReplay = false;

		StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "journal")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( JournalScene.class );
			}
		};
		btnBadges.icon(Icons.get(Icons.JOURNAL));
		add(btnBadges);

//		StyledButton btnNews = new NewsButton(GREY_TR, Messages.get(this, "news"));
//		btnNews.icon(Icons.get(Icons.NEWS));
//		add(btnNews);

		StyledButton btnChanges = new ChangesButton(GREY_TR, Messages.get(this, "changes"));
		btnChanges.icon(Icons.get(Icons.CHANGES));
		add(btnChanges);

		StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_TR, Messages.get(this, "about")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchScene( AboutScene.class );
			}
		};
		btnAbout.icon(Icons.get(Icons.ARRANGED));
		add(btnAbout);
		
		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		float buttonAreaWidth = landscape() ? PixelScene.MIN_WIDTH_L-6 : PixelScene.MIN_WIDTH_P-2;
		float btnAreaLeft = (Camera.main.width - buttonAreaWidth) / 2f;
		if (landscape()) {
			btnPlay.setRect(btnAreaLeft, topRegion+GAP, (buttonAreaWidth/2)-1, BTN_HEIGHT);
			align(btnPlay);
			btnSupport.setRect(btnPlay.right()+2, btnPlay.top(), btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnSettings.setRect(btnRankings.left(), btnRankings.bottom() + GAP, (int)(btnRankings.width()*0.67f)-1, BTN_HEIGHT);
			btnChanges.setRect(btnSettings.right()+2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnChanges.right()+2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
		} else {
			btnPlay.setRect(btnAreaLeft, topRegion+GAP, buttonAreaWidth, BTN_HEIGHT);
			align(btnPlay);
			btnSupport.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnSupport.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);

			btnBadges.setRect(btnPlay.left(), btnRankings.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
			btnChanges.setRect(btnBadges.right()+2, btnBadges.top(), btnBadges.width(), BTN_HEIGHT);
			btnSettings.setRect(btnBadges.left(), btnBadges.bottom()+GAP, btnBadges.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.right()+2, btnSettings.top(), btnBadges.width(), BTN_HEIGHT);
		}

        StyledButton btnLocalization = new LocalizationButton(GREY_TR, "译者注");
        add(btnLocalization);
        btnLocalization.setRect(0, h - BTN_HEIGHT, 50, BTN_HEIGHT);

		StyledButton btnSeedFinder = new SeedFinderButton(GREY_TR, "查种系统");
		add(btnSeedFinder);
		btnSeedFinder.setRect(0, 0, 60, BTN_HEIGHT);

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

		if (DeviceCompat.isDesktop()) {
			ExitButton btnExit = new ExitButton();
			btnExit.setPos( w - btnExit.width(), 0 );
			add( btnExit );
		}

		Badges.loadGlobal();
		if (Badges.isUnlocked(Badges.Badge.VICTORY) && !SPDSettings.victoryNagged()) {
			SPDSettings.victoryNagged(true);
			add(new WndVictoryCongrats());
		}

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.x = x - fb.width()/2f;
		fb.y = y - fb.height();

		align(fb);
		add( fb );
	}

	private static class NewsButton extends StyledButton {

		public NewsButton(Chrome.Type type, String label ){
			super(type, label);
			if (SPDSettings.news()) News.checkForNews();
		}

		int unreadCount = -1;

		@Override
		public void update() {
			super.update();

			if (unreadCount == -1 && News.articlesAvailable()){
				long lastRead = SPDSettings.newsLastRead();
				if (lastRead == 0){
					if (News.articles().get(0) != null) {
						SPDSettings.newsLastRead(News.articles().get(0).date.getTime());
					}
				} else {
					unreadCount = News.unreadArticles(new Date(SPDSettings.newsLastRead()));
					if (unreadCount > 0) {
						unreadCount = Math.min(unreadCount, 9);
						text(text() + "(" + unreadCount + ")");
					}
				}
			}

			if (unreadCount > 0){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			super.onClick();
			ShatteredPixelDungeon.switchNoFade( NewsScene.class );
		}
	}

	private static class ChangesButton extends StyledButton {

		public ChangesButton( Chrome.Type type, String label ){
			super(type, label);
			if (SPDSettings.updates()) Updates.checkForUpdate();
		}

		boolean updateShown = false;

		@Override
		public void update() {
			super.update();

			if (!updateShown && Updates.updateAvailable()){
				updateShown = true;
				text(Messages.get(TitleScene.class, "update"));
			}

			if (updateShown){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Updates.updateAvailable()){
				AvailableUpdateData update = Updates.updateData();

				ShatteredPixelDungeon.scene().addToFront( new WndOptions(
						Icons.get(Icons.CHANGES),
						update.versionName == null ? Messages.get(this,"title") : Messages.get(this,"versioned_title", update.versionName),
						update.desc == null ? Messages.get(this,"desc") : update.desc,	//업데이트 문장이 없다면 내장 문장 출력, 받아온 문장이 있다면 받아온 문장을 출력
						Messages.get(this,"update"),
						Messages.get(this,"playstore"),
						Messages.get(this,"changes")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Updates.launchUpdate(Updates.updateData());
						} else if (index == 1){
							String linkUrl = "https://play.google.com/store/apps/details?id=com.rearrangedpixel.rearrangedpixeldungon";
							ShatteredPixelDungeon.platform.openURI( linkUrl );
						} else if (index == 2){
							ShatteredPixelDungeon.switchNoFade( ChangesScene.class );
						}
					}
				});

			} else {
				ShatteredPixelDungeon.switchNoFade( ChangesScene.class );
			}
		}

	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton( Chrome.Type type, String label ){
			super(type, label);
			if (Messages.lang().status() == Languages.Status.X_UNFINISH){
				icon(Icons.get(Icons.LANGS));
				icon.hardlight(1.5f, 0, 0);
			} else {
				icon(Icons.get(Icons.PREFS));
			}
		}

		@Override
		public void update() {
			super.update();

			if (Messages.lang().status() == Languages.Status.X_UNFINISH){
				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Messages.lang().status() == Languages.Status.X_UNFINISH){
				WndSettings.last_index = 5;
			}
			ShatteredPixelDungeon.scene().add(new WndSettings());
		}
	}

	private static class SupportButton extends StyledButton{

		public SupportButton( Chrome.Type type, String label ){
			super(type, label);
			icon(Icons.get(Icons.DISCORD));
			textColor(Window.TITLE_COLOR);
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.switchNoFade(SupporterScene.class);
		}
	}

	private static class SeedFinderButton extends StyledButton {
		public SeedFinderButton(Chrome.Type type, String label) {
			super(type, label);
			icon(Icons.get(Icons.SEED));
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
		}
	}

    private static class LocalizationButton extends StyledButton {
        public LocalizationButton(Chrome.Type type, String label) {
            super(type, label);
            icon(Icons.get(Icons.LANGS));
        }

        @Override
        protected void onClick() {
            ShatteredPixelDungeon.scene().add(new WndLocalization());
        }

        private static class WndLocalization extends Window {

            private static final int WIDTH_P = 120;
            private static final int WIDTH_L = 144;
            private static final int MARGIN = 4;

            private static final int BTN_HEIGHT = 20;

            public WndLocalization() {
                super();

                String text =  "_介是个嘛玩意儿？_\n" +
                        "ReARrangedPixelDungeon，枪火地牢重置版，简称rar地牢(zip:?)。由于作者cocoa在枪火地牢(以下简写为ar地牢)里复制粘贴了海量代码导致结构极其臃肿，在痛定思痛/闲的没事/想再加新的发现要cv的越来越多后，决定另起炉灶，用新的代码架构(不等于更少的bug)来实现ar地牢的内容，同时对ar一些起码作者觉得不好的东西进行优化和平衡。目前距离ar地牢的完整功能还有一定距离(骑士和护士)，但在可预见的将来会有更新。\n" +
                        "_咋现在才有翻译？_\n" +
                        "作者毕竟是韩国人，翻译人员的英语能力可能还可以达到一般水平，但韩语基本上两眼一抹黑。实际上本次翻译也是在机翻韩语+查看作者机翻韩语出来的英语+代码反向分析+参考ar地牢翻译完成的。鸽人能力有限，翻译工作量大且时间仓促，难以面面俱到，恐怕到处都是疏漏之处，如有意见与反馈，请通过下方联系方式接洽。由于个人时间有限而且rar将来肯定会更新，后续工作仍然存在人手缺乏问题，如有意向协助翻译，亦可通过联系方式讨论具体事宜。\n" +
                        "QQ：1015561267\n" +
                        "QQ群：460655540\n" +
                        "github项目地址：https://github.com/ \n" + "1015561267/Re-ARranged-Pixel-Dungeon-CN\n" +
                        "_所以说这么多是要干嘛？_\n" +
                        "在游玩中可能会发现实际效果与文本描述有所出入(实际上作者已经这么干过不知道多少回了)，但译者仅进行翻译工作，程序性bug不在职责范围之内。左下角子页面列出当前版本（3.3.0）的已知bug，作为免责声明。\n"+
                        "——by _Teller_\n";

                RenderedTextBlock.cnLocalizationFlag = true;

                RenderedTextBlock info = PixelScene.renderTextBlock( text, 6 );
                info.maxWidth((PixelScene.landscape() ? WIDTH_L : WIDTH_P) - MARGIN * 2);
                info.setPos(MARGIN, MARGIN);
                add( info );

                RedButton bugButton = new BugButton("bug");
                add(bugButton);
                bugButton.setRect(0, info.height() + MARGIN, 50, BTN_HEIGHT);

                RedButton recipeButton =  new RecipeButton("???");
                add(recipeButton);
                recipeButton.setRect(info.width() - MARGIN - 50, info.height() + MARGIN, 50, BTN_HEIGHT);

                resize(
                        (int) (info.width() + MARGIN * 2),
                        (int) (info.height() + BTN_HEIGHT + MARGIN * 3));

                RenderedTextBlock.cnLocalizationFlag = false;
            }


            private static class BugButton extends RedButton {
                public BugButton(String text) {
                    super(text);
                }

                @Override
                protected void onClick() {
                    RenderedTextBlock.cnLocalizationFlag = true;
                    ShatteredPixelDungeon.scene().add(new WndMessage(
                            "- _陈旧护符只能对女猎弓，探险家铲子或砍刀，决斗任意非枪械近战或骑士盾使用，其他的作者还没做_\n"+

                            "以下内容经过代码分析与实际游玩确认存在，与翻译无关，以当前版本(3.11.1)为准，不排除随着后续更新而修复的可能。\n\n" +
                                    "- 格斗家的无阻腕动可以影响包括投掷武器的一切物理攻击\n" +
                                    "- 所有投掷武器的精准度严重异常，非枪支子弹近距离变成25%精度而远距离是225%精度\n" +
                                    "- _武士的枪支射击一旦触发暴击，其伤害加成基准并非子弹，而是同阶的投掷武器模板_。导致暴击时机枪与冲锋枪伤害不正常的高，而榴弹火箭筒以及狙在没有任何加成的情况下输出反而可能会比不暴击的低，而霰弹枪的加成最大，多发弹丸与低单发伤害导致其暴击时的伤害期望至少+100%。突击步枪基本不受影响。\n" +
                                    "- _警告！此bug严重影响平衡性_大师只要在纳刀状态下且快斩不在冷却，任何试图调起暴击率计算式的方法(比如攻击和反复开关武器信息面板)都会导致时间倒退，造成包括但不限于身上buff与debuff异常延长(包括霰弹枪快斩暴击后纳刀在接近10回合内不会自动解除)，敌人长时间无法行动等各种严重问题。\n" +
                                    "- 工程师的迫击炮+2无效，而加农炮的+2同时影响了加农炮和迫击炮(cocoa写的天赋多，天赋效果贴岔的也多)\n"+
                                    "- 探索者的藤条大师天赋效果非常混乱，藤条可以拖敌人和地形移动，但+1 +2仅对拖敌人有用\n" +
                                    "- 探索者的藤条束缚仍然会消耗，而且由于程序次序问题，在不足5根藤条的情况下对邻近敌人使用会无限反复触发2-5\n" +
                                    "- 复春之书的显示和效果不一致，植物生成数量很难达到期望值\n" +
                                    "- 砍刀在对超出距离的非敌方单位使用时没有效果，但饥饿值会-1，而砍草并不会减少饥饿值。\n" +
                                    "- 幽灵骑士3-5天赋效果如括号内，减伤效果莫名其妙写到3-4里面了但3-4原效果也有效。\n" +
                                    "- 还有一个不能算bug的bug：解锁武士需要的武士刀并不包括evan破碎原版的武士刀。(what can I say)\n" , 4
                    ));
                    RenderedTextBlock.cnLocalizationFlag = false;
                }
            }

            private static class RecipeButton extends RedButton {
                public RecipeButton(String text) {
                    super(text);
                }

                @Override
                protected void onClick() {
                    RenderedTextBlock.cnLocalizationFlag = true;
                    ShatteredPixelDungeon.scene().add(new WndMessage(
                                    "_警告！_以下内容可能破坏作者提供的解谜体验\n\n" +
                                    "cocoa用武器蓝图替代了大量炼金武器配方，不过只有正确的近战武器组合才能出正确的蓝图，而且还要献祭一把手头的武器，还有概率失败。\n" +
                                    "- 矛与盾 = 长矛 + 圆盾 + 进化菱晶\n" +
                                    "- 真符之刃 = 符文之刃 + 升级催化剂 + 进化菱晶\n" +
                                    "- 无形之刃 = 暗杀之刃 + 升级催化剂 + 进化菱晶\n" +
                                    "- 链鞭 = 长鞭 + 升级催化剂 + 进化菱晶\n" +
                                    "- 链锤 = 链鞭 + 链枷 + 进化菱晶\n" +
                                    "- 撒旦圣经 = 圣经 + 升级催化剂 + 进化菱晶\n" +
                                    "- 刺杀之矛 = 关刀 + 暗杀之刃 + 进化菱晶\n" +
                                    "- 力量手套 = 魔岩拳套 + 震爆方石 + 进化菱晶\n"+
                                    "- 光之对剑 = 魔岩拳套 + 升级催化剂 + 进化菱晶\n"+
                                    "- 工匠之锤 = 战锤 + 升级催化剂 + 进化菱晶\n" +
                                    "- 村正 = 太刀 + 升级催化剂 + 进化菱晶\n" +
                                    "- 巨型双剑 = 巨剑 + 巨剑 + 进化菱晶\n" +
                                    "- 重剑 = 巨剑 + 升级催化剂 + 进化菱晶\n" +
                                    "- 沉重的剑(力量24后变成圣剑)= 圣经 + 重剑 + 进化菱晶\n" +
                                    "- 黑曜石盾 = 巨型方盾 + 升级催化剂 + 进化菱晶\n" +
                                    "- 骑枪 = 关刀 + 升级催化剂 + 进化菱晶\n" +
                                    "- 骑枪与黑盾 = 骑枪 + 黑曜石护盾 + 进化菱晶\n" +
                                    "- 战术手枪 = 强化手枪 + 升级催化剂 + 进化菱晶\n" +
                                    "- 战术火箭筒 = 强化火箭筒 + 升级催化剂 + 进化菱晶\n" +
                                    "- 战术狙击枪 = 强化狙击枪 + 升级催化剂 + 进化菱晶\n" +
                                    "- 战术榴弹发射器 = 强化榴弹发射器 + 升级催化剂 + 进化菱晶\n" +
                                    "- 战术突击步枪 = 强化突击步枪 + 升级催化剂 + 进化菱晶\n"+
                                    "- 防弹盾 = 战术手枪 + 黑曜石盾 + 进化菱晶\n"
                    ));
                    RenderedTextBlock.cnLocalizationFlag = false;
                }
            }
        }
    }
}
