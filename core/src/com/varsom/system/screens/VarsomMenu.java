package com.varsom.system.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.varsom.system.VarsomSystem;
import com.varsom.system.abstract_gameobjects.VarsomGame;
import com.varsom.system.games.car_game.com.varsomgames.cargame.CarGame;
import com.varsom.system.games.other_game.OtherGame;

/**
 * Created by oskarcarlbaum on 16/04/15.
 */
public class VarsomMenu implements Screen {

    private Stage stage = new Stage();
    private Table table = new Table();

    private final int WIDTH = Gdx.graphics.getWidth();
    private final int HEIGHT = Gdx.graphics.getHeight();

    //TODO Load files from a SystemAssetLoader. Also, create a folder and skin for the varsom system
    private Skin skin = new Skin(Gdx.files.internal("system/skins/menuSkin.json"), new TextureAtlas(Gdx.files.internal("system/skins/menuSkin.pack")));
    private Skin skin2 = new Skin(Gdx.files.internal("data/uiskin.json"));

    private TextButton buttonExit = new TextButton("Exit system", skin);
    private Label ips;

    protected VarsomSystem varsomSystem;

    public VarsomMenu(VarsomSystem varsomSystem){
        this.varsomSystem = varsomSystem;

    }

    @Override
    public void show() {
        //For every VarsomeGame in the game array create a button
        ips = new Label("Server IP's:\n" + varsomSystem.getServerIP(),skin2);
        table.add(ips).size(600,90).padBottom(80).row();
        for(int i = 0; i < VarsomSystem.SIZE; i++) {
            switch (VarsomSystem.games[i]){

                //Cargame
                case 1:
                    TextButton buttonPlayCarGame = new TextButton("Start CarGame", skin);

                    buttonPlayCarGame.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                           // if(varsomSystem.getServer().getConnections().length != 0) {
                                VarsomGame carGame = new CarGame((VarsomSystem) Gdx.app.getApplicationListener());
                                hide();
                            /*}
                            else{
                                AlertDialog aD = new AlertDialog("Too few players", skin2);
                                aD.show(stage);
                            }*/
                            Gdx.app.log("clicked", "pressed CarGame");

                            //((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1));
                        }
                    });
                    table.add(buttonPlayCarGame).size(450, 90).padBottom(10).row();
                    break;

                //OtherGame
                case 2:
                    TextButton buttonPlayOtherGame = new TextButton("Start OtherGame", skin);

                    buttonPlayOtherGame.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            VarsomGame OtherGame = new OtherGame((Game) Gdx.app.getApplicationListener());
                            Gdx.app.log("clicked", "pressed OtherGame.");
                            hide();
                        }
                    });
                    table.add(buttonPlayOtherGame).size(450, 90).padBottom(80).row();
                    break;

                //If something goes wrong
                default:
                    Gdx.app.log("in game array", "wrong game ID");

            }

        }

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "pressed the EXIT SYSTEM button.");
                Gdx.app.exit();
                dispose();
            }
        });

        table.add(buttonExit).size(450, 90).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public static class AlertDialog extends Dialog {

        public AlertDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }
        public AlertDialog(String title, Skin skin) {
            super(title, skin);
        }
        public AlertDialog(String title, WindowStyle windowStyle) {
            super(title, windowStyle);
        }

        {
            text("CarGame requiers at least two connected players");
            button("OK");
        }

        @Override
        protected void result(Object object){

        }
    }
}
