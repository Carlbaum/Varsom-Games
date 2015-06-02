package com.varsom.system.games.car_game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.varsom.system.Commons;
import com.varsom.system.VarsomSystem;
import com.varsom.system.games.car_game.helpers.AssetLoader;
import com.varsom.system.games.car_game.helpers.KrazyRazyCommons;
import com.varsom.system.network.NetworkListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import java.util.Vector;

public class ResultScreen extends ScaledScreen {

    protected VarsomSystem varsomSystem;
    private ArrayList<String> carOrder;

    //TODO Load files from AssetLoader
    private Skin skin = AssetLoader.skin;

   // private TextButton btnOK;
    private Label result, winningPlayer, score, proceed;
    private Vector<Label> playerStandings;
    private Image smileyMad, smileyHappy;
    private float frameCounter = 0;

    //private String playerScores;

    public ResultScreen(VarsomSystem varsomSystem, String names) {
        this.varsomSystem = varsomSystem;
        carOrder = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(names, "\n");
        while(st.hasMoreTokens()){
            carOrder.add(st.nextToken());
        }

        playerStandings = new Vector<Label>();

        //Switch screen on the controller to NavigationScreen
        varsomSystem.getMPServer().changeScreen(Commons.NAVIGATION_SCREEN);
    }

    @Override
    public void show() {

        generateUI();

        stage.addActor(winningPlayer);
       // stage.addActor(result);
        //stage.addActor(score);
        stage.addActor(proceed);
        stage.addActor(smileyHappy);
        stage.addActor(smileyMad);

        for(int i = 0; i < playerStandings.size(); i++){

            stage.addActor(playerStandings.get(i));

        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(24 / 255.0f, 102 / 255.0f, 105 / 255.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleDpad();
        handleScore();

        frameCounter += Gdx.graphics.getDeltaTime();
        proceed.setPosition(proceed.getX(), proceed.getY() + 0.2f * (float) Math.sin(frameCounter));

        stage.act();
        stage.draw();
    }

    public void generateUI(){

        //Font
        BitmapFont winningFont = Commons.getFont(150, AssetLoader.krazyFontFile, KrazyRazyCommons.KRAZY_GREEN, 3f, KrazyRazyCommons.KRAZY_BLUE);
        BitmapFont playerStandingFont = Commons.getFont(100, AssetLoader.krazyFontFile,KrazyRazyCommons.KRAZY_BLUE, 3f, KrazyRazyCommons.KRAZY_GREEN);

        Label.LabelStyle styleWinning = new Label.LabelStyle(winningFont, Color.WHITE);
        Label.LabelStyle stylePlayerStanding = new Label.LabelStyle(playerStandingFont, Color.WHITE);

        smileyHappy = new Image(AssetLoader.krazyThingyTexture_1);
        smileyMad = new Image(AssetLoader.krazyThingyTexture_mad);
        smileyMad.setScale(1.5f, 1.5f);

        //Title
        winningPlayer = new Label(carOrder.get(0) + " is victorious!", styleWinning);
        winningPlayer.setPosition(Commons.WORLD_WIDTH /2 - winningPlayer.getWidth()/2, Commons.WORLD_HEIGHT - winningPlayer.getHeight());

        smileyHappy.setPosition(winningPlayer.getX() - smileyHappy.getWidth() - 50, winningPlayer.getY() + smileyHappy.getHeight() / 2 - 20);
        smileyMad.setPosition(Commons.WORLD_WIDTH * 0.8f, Commons.WORLD_HEIGHT / 2 - smileyMad.getHeight() / 2);

        for(int i = 1; i < carOrder.size(); i++){

            Label playerStanding = new Label( (i + 1) + ". " + carOrder.get(i), stylePlayerStanding ) ;
            playerStanding.setPosition(Commons.WORLD_WIDTH / 2 - playerStanding.getWidth() / 2, Commons.WORLD_HEIGHT - winningPlayer.getHeight() - playerStanding.getHeight()*i  );

            playerStandings.add(playerStanding);

        }

/*
        result = new Label(carOrder.get(0) + " is VICTORIOUS!!", skin);
        result.setPosition(Commons.WORLD_WIDTH / 2 - result.getWidth() / 2, Commons.WORLD_HEIGHT * 0.8f - result.getHeight());
        score = new Label(playerScores, skin);
        score.setPosition(Commons.WORLD_WIDTH / 2 - score.getWidth() / 2, Commons.WORLD_HEIGHT * 0.6f - score.getHeight());*/

        proceed = new Label("Press anywhere to continue", stylePlayerStanding);
        proceed.setPosition(Commons.WORLD_WIDTH / 2 - proceed.getWidth() / 2, Commons.WORLD_HEIGHT * 0.2f);

      /*  btnOK = new TextButton("OK", skin);
        btnOK.setPosition(Commons.WORLD_WIDTH / 2 - btnOK.getWidth() / 2, Commons.WORLD_HEIGHT * 0.2f);*/

      /*  proceed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "pressed the OK button.");
                varsomSystem.getMPServer().setJoinable(true);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(varsomSystem));
            }
        });*/

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

    public void handleDpad() {

        if (NetworkListener.dPadSelect) {
            Gdx.app.log("clicked", "pressed the OK button.");
            varsomSystem.getMPServer().setJoinable(true);
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(varsomSystem));

            NetworkListener.dPadSelect = false;
        }
    }

    // A later feature
    private void handleScore(){

        /*playerScores = ": Name : Time/Score/Dist : Knockouts :\n";

        for(String car : carOrder) {
            Gdx.app.log("handleScore " + carOrder., varsomSystem.getServer().getConnections()[carList.get(i).getID()].toString());
            //Ranking order
            playerScores += varsomSystem.getServer().getConnections()[carList.get(i).getID()].toString() + " : ";
            //Points or time
            playerScores += carList.get(i).getTraveledDistance() + " : ";
            //Knockouts
            playerScores += "- : \n";
        }

        score.setText(playerScores);*/

    }
}

/*

public class WinScreen extends ScaledScreen {

    public void render(){

        Gdx.gl.glClearColor(0.12f, 0.12f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for(Car car : carList) {
            //Ranking order
            players += varsomSystem.getServer().getConnections()[car.getID()].toString() + " : ";
            //Points or time
            players += car.getTraveledDistance() + " : ";
            //Knockouts
            players += "- : ";
            //OK button

        }

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
}
*/