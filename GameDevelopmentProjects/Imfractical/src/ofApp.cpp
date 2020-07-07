#include "ofApp.h"
#include "Emitter.h"
#include "States.h"
//----------------------------------------------------------------------------------
//
// 
//	2D Shooter Game Project
//	This variant is more along the lines of a 2D platformer based shooter
//	It is in the style of a boss rush with 3 Levels
//	Each boss has different attack patterns and sprites
//	A tutorial is included
// 
// 
//  Player Controls:
//	Left right arrows to move
//	Up/space to jump (light tap to short hop, double tap to double jump)
//	Down to fast fall
//  z key to projectile attack
//  x key to short range attack
//  
//
//	Anhduy Khong CS134 - SJSU
// 3/31/19

//--------------------------------------------------------------
void ofApp::setup(){
	// Initialize flag values
	gameState = TITLE; // keeps track of what state the game is in
	start = false; // keeps track of whether or not objects should be moving and updated on screen
	bZKeyDown = false;
	bXKeyDown = false;
	bSpaceKeyDown = false;
	score = 0;
	ofSetVerticalSync(true);
	ofBackground(0,0,0);
	gravityForce = new GravityForce(ofVec3f(0, 800, 0)); //Initialize forces
	impulseForce = new ImpulseRadialForce(50000.0); //Initialize forces

	// Initialize final emitters to emit fireworkesque particles on clear
	endEmitter1 = new Emitter(new SpriteSystem(), new ParticleSystem);
	endEmitter1->generateAndAddParticle("images/particle_sprite_sheet.png");
	endEmitter1->particles[0].sprite.setAnim(10, 10, 1, 1, 0, 0, 1, 5000);
	endEmitter1->partsys->addForce(impulseForce);
	endEmitter1->setPosition(ofVec3f(ofGetWindowWidth() / 8, ofGetWindowHeight() / 4.0, 0));
	endEmitter1->groupSize = 20;
	endEmitter1->childLifespan = 1000;

	endEmitter2 = new Emitter(new SpriteSystem(), new ParticleSystem);
	endEmitter2->generateAndAddParticle("images/particle_sprite_sheet.png");
	endEmitter2->particles[0].sprite.setAnim(10, 10, 1, 1, 0, 0, 1, 5000);
	endEmitter2->partsys->addForce(impulseForce);
	endEmitter2->setPosition(ofVec3f(7 * ofGetWindowWidth() / 8, ofGetWindowHeight() / 4.0, 0));
	endEmitter2->groupSize = 20;
	endEmitter1->childLifespan = 1000;

	// Initialize player emitter
	emitter = new PlayerEmitter(new SpriteSystem(), new ParticleSystem(), "images/player_sprite_sheet.png");
	emitter->setPosition(ofVec3f(ofGetWindowWidth() / 2, ofGetWindowHeight() - emitter->height / 2, 0));
	emitter->generateAndAddParticle("images/particle_sprite_sheet.png");
	emitter->particles[0].sprite.setAnim(10, 10, 1, 1, 0, 0, 1, 5000);
	emitter->partsys->addForce(impulseForce);

	//Initialize first boss emitter
	boss1Emitter = new Boss1Emitter(new SpriteSystem(), new ParticleSystem(), new EmitterSystem(), "images/boss1_sprite_sheet.png");
	boss1Emitter->setPosition(ofVec3f(ofGetWindowWidth() - boss1Emitter->width / 2, ofGetWindowHeight() - boss1Emitter->height / 2, 0));

	//Initialize second boss emitter and corresponding secondary emitters
	boss2Emitter = new Boss2Emitter(new SpriteSystem(), new ParticleSystem(), "images/boss2_sprite_sheet.png");
	boss2Emitter->setPosition(ofVec3f(ofGetWindowWidth() - boss2Emitter->width / 2, ofGetWindowHeight() - boss2Emitter->height / 2, 0));
	generateLevel2Emitters(2, level2Emitters);
	level2Emitters[0]->setPosition(ofVec3f(level2Emitters[0]->width / 2, -level2Emitters[0]->height / 2, 0));
	level2Emitters[1]->setPosition(ofVec3f(ofGetWindowWidth() - level2Emitters[1]->width / 2, -level2Emitters[1]->height / 2, 0));

	//Initialize third boss emitter and corresponding secondary emitters
	boss3Emitter = new Boss3Emitter(new SpriteSystem(), new ParticleSystem(), "images/boss3_sprite_sheet.png");
	boss3Emitter->setPosition(ofVec3f(ofGetWindowWidth()/2, ofGetWindowHeight() - boss3Emitter->height / 2, 0));
	boss3Emitter->partsys->addForce(gravityForce);
	generateLevel3Emitters();
	topL3Emitters[0]->setPosition(ofVec3f(topL3Emitters[0]->width / 2, -topL3Emitters[0]->height / 2, 0));
	topL3Emitters[1]->setPosition(ofVec3f(ofGetWindowWidth()/2 + topL3Emitters[1]->width / 2, -topL3Emitters[1]->height / 2, 0));
	leftL3Emitters[0]->setPosition(ofVec3f(-leftL3Emitters[0]->width/2, leftL3Emitters[0]->height/2, 0));
	leftL3Emitters[1]->setPosition(ofVec3f(-leftL3Emitters[1]->width/2,ofGetWindowHeight() -  leftL3Emitters[1]->height / 2, 0));
	rightL3Emitters[0]->setPosition(ofVec3f(ofGetWindowWidth() + rightL3Emitters[0]->width/2, rightL3Emitters[0]->height / 2, 0));
	rightL3Emitters[1]->setPosition(ofVec3f(ofGetWindowWidth() + rightL3Emitters[1]->width / 2, ofGetWindowHeight() - rightL3Emitters[1]->height / 2, 0));


	//loads all the sound and music
	player.load("sfx/explosion.wav");
	menuBgm.load("sfx/arpbounce.mp3");
	level1Bgm.load("sfx/anewleaf.mp3");
	level2Bgm.load("sfx/synergy.mp3");
	level3Bgm.load("sfx/universal.mp3");
	clearBgm.load("sfx/festival.mp3");
	menuBgm.play();
	menuBgm.setLoop(true);
	level1Bgm.setLoop(true);
	level2Bgm.setLoop(true);
	level3Bgm.setLoop(true);
	clearBgm.setLoop(true);

	// This block loads all the images and sets them to corresponding entities
	background.loadImage("images/background2.png");
	title.loadImage("images/title.png");
	tutorial1.loadImage("images/sample.png");
	tutorial2.loadImage("images/sample2.png");
	tutorial3.loadImage("images/sample3.png");
	menu.loadImage("images/main_menu.png");
	level1Clear.loadImage("images/level1Clear.png");
	level2Clear.loadImage("images/level2Clear.png");
	gameOver.loadImage("images/game_over.png");

	//gui.setup();
	//gui.add(rate.setup("rate", 15, 1, 20));
	//gui.add(life.setup("life", 2, .1, 10));
	//gui.add(velocity.setup("velocity", ofVec3f(0, -1500, 0), ofVec3f(-2000, -2000, -1000), ofVec3f(2000, 2000, 1000)));

	//bHide = false;

	// Loads the font
	font1.load("fonts/Exo2-Black.otf", 30);
	font2.load("fonts/Exo2-Black.otf", 60);
}

//--------------------------------------------------------------
void ofApp::update() {
	if (start) {
		emitter->update();
		switch (gameState) {
		// Updates boss of level 1
		// Sets phases accordingly and checks for collision
		case LEVEL1: {
			updateBoss1Emitter();
			if (boss1Emitter->life < boss1Emitter->maxLife / 2 && boss1Emitter->phase == PHASE1) {
				boss1Emitter->setPhase2();
			}
			if (boss1Emitter->life <= 0) {
				setLevel1Clear();
			}
			checkCollisionsLevel1();
			break;
		}
		// Updates boss of level 2
		// Sets phases accordingly and checks for collision
		// Phase 3 utilizes secondary emitters to spawn projectiles. The emitters move and emit particles which have gravityforce applied to them
		case LEVEL2: {
			updateBoss2Emitter();
			updateLevel2Emitters();
			if (boss2Emitter->life < 2 * (boss2Emitter->maxLife / 3) && boss2Emitter->phase == PHASE1) {
				boss2Emitter->setPhase2();
			}
			else if (boss2Emitter->life < boss2Emitter->maxLife / 3 && boss2Emitter->phase == PHASE2) {
				boss2Emitter->setPhase3();
				for (int i = 0; i < level2Emitters.size(); i++) {
					level2Emitters[i]->start();
					level2Emitters[i]->velocity.x = 600;
				}
			}
			if (boss2Emitter->life <= 0) {
				setLevel2Clear();
			}
			checkCollisionsLevel2();
			break;
		}
		// Updates boss of level 3
		// Sets phases accordingly and checks for collision
		// Each odd phase is an attack pattern
		// Each even phase takes the previous phase's attack pattern and freezes the projectiles/particles every 6 seconds, and when unfrozen the projectiles
		// behave depending on the phase
		// Phases 7 and up utilize secondary emitters
		// Phase 1 = boss targets player with projectiles
		case LEVEL3: {
			updateBoss3Emitter();
			updateTopL3Emitters();
			updateLeftL3Emitters();
			updateRightL3Emitters();
			if (boss3Emitter->life < 11 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE1) {
				boss3Emitter->setPhase2(); //  Phase 2 = projectile freezing of phase 1, then retargets player
			}
			else if (boss3Emitter->life < 10 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE2) {
				boss3Emitter->setPhase3(); // Phase 3 = fountain effect of projectiles, uses particle physics with gravity
			}
			else if (boss3Emitter->life < 9 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE3) {
				boss3Emitter->setPhase4(); // Phase 4 = projectile freezing of phase 3, then retargets player
			}
			else if (boss3Emitter->life < 8 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE4) {
				boss3Emitter->setPhase5(); // Phase 5 = shoots projecties at ground level and boss height so players must stay in between heights and short hop
			}
			else if (boss3Emitter->life < 7 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE5) {
				boss3Emitter->setPhase6(); // Phase 6 = projectile freezing of phase 5, then retargets player
			}
			else if (boss3Emitter->life < 6 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE6) {
				boss3Emitter->setPhase7(); // Phase 7 = same pattern as phase 1
				for (int i = 0; i < topL3Emitters.size(); i++) { // Begins dropping particles from the top
					topL3Emitters[i]->start();
					topL3Emitters[i]->velocity.x = 300;
					topL3Emitters[i]->childSpawnRate = 4;
					topL3Emitters[i]->particles[0].sprite.setAnim(80, 80, 1, 1, 480, 0, 1, topL3Emitters[i]->childLifespan);
				}
			}
			else if (boss3Emitter->life < 5 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE7) {
				boss3Emitter->setPhase8(); // Phase 8 = projectile freezing of phase 7, then projectiles move inwards
				for (int i = 0; i < topL3Emitters.size(); i++) {
					topL3Emitters[i]->particles[0].sprite.setAnim(80, 80, 1, 1, 560, 0, 1, topL3Emitters[i]->childLifespan);
				}
			}
			else if (boss3Emitter->life < 4 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE8) {
				boss3Emitter->setPhase9(); // Phase 9 = projectiles come from the right and left
				for (int i = 0; i < topL3Emitters.size(); i++) {
					topL3Emitters[i]->stop();
					topL3Emitters[i]->velocity.x = 0;
				}
				for (int i = 0; i < leftL3Emitters.size(); i++) {
					leftL3Emitters[i]->start();
					leftL3Emitters[i]->childSpawnRate = 2;
					leftL3Emitters[i]->velocity.y = 300;
					leftL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 640, 0, 1, leftL3Emitters[i]->childLifespan);
				}
				for (int i = 0; i < rightL3Emitters.size(); i++) {
					rightL3Emitters[i]->start();
					rightL3Emitters[i]->childSpawnRate = 2;
					rightL3Emitters[i]->velocity.y = 300;
					rightL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 640, 0, 1, rightL3Emitters[i]->childLifespan);
				}
			}
			else if (boss3Emitter->life < 3 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE9) {
				boss3Emitter->setPhase10(); // Phase 10 = projectile freezing of phase 9, then projectiles fall down
				for (int i = 0; i < leftL3Emitters.size(); i++) {
					leftL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 720, 0, 1, leftL3Emitters[i]->childLifespan);
				}
				for (int i = 0; i < rightL3Emitters.size(); i++) {
					rightL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 720, 0, 1, rightL3Emitters[i]->childLifespan);
				}
			}
			else if (boss3Emitter->life < 2 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE10) {
				boss3Emitter->setPhase11(); // Phase 11 = all secondary emitters are active
				for (int i = 0; i < topL3Emitters.size(); i++) {
					topL3Emitters[i]->start();
					topL3Emitters[i]->childSpawnRate = 2;
					topL3Emitters[i]->velocity.x = 300;
					topL3Emitters[i]->particles[0].sprite.setAnim(80, 80, 1, 1, 800, 0, 1, topL3Emitters[i]->childLifespan);
				}
				for (int i = 0; i < leftL3Emitters.size(); i++) {
					leftL3Emitters[i]->childSpawnRate = 1.5;
					leftL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 800, 0, 1, leftL3Emitters[i]->childLifespan);
				}
				for (int i = 0; i < rightL3Emitters.size(); i++) {
					rightL3Emitters[i]->childSpawnRate = 1.5;
					rightL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 800, 0, 1, rightL3Emitters[i]->childLifespan);
				}
			}
			else if (boss3Emitter->life <= 1 * (boss3Emitter->maxLife / 12) && boss3Emitter->phase == PHASE11) {
				boss3Emitter->setPhase12(); // Phase 12 = large projectile that moves from right to left, player must attack it with short range attacks to push it back
				for (int i = 0; i < topL3Emitters.size(); i++) {
					topL3Emitters[i]->stop();
					topL3Emitters[i]->velocity.x = 0;
				}
				for (int i = 0; i < leftL3Emitters.size(); i++) {
					leftL3Emitters[i]->stop();
					leftL3Emitters[i]->velocity = ofVec3f(0, 0, 0);
				}
				for (int i = 0; i < rightL3Emitters.size(); i++) {
					rightL3Emitters[i]->stop();
					rightL3Emitters[i]->velocity = ofVec3f(0, 0, 0);
				}
				boss3Emitter->Emitter::stop();
			}
			else if (boss3Emitter->life <= 0 && boss3Emitter->phase == PHASE12) {
				boss3Emitter->lastProjectile.velocity = ofVec3f(0, 0, 0);
				setLevel3Clear();
			}
			checkCollisionsLevel3();
			break;
		}
		}
	}
	else {
		//Updates emitters for fireworks effect if game is cleared
		if (gameState == LEVEL3CLEAR) {
		updateEndEmitters();
		}
	}
}


//--------------------------------------------------------------
void ofApp::draw(){
	if (start) {
		// Draw all images while the game is started
		// Draws specific bosses depending on the level
		// Level sates have helper strings that warn the player about the phase
		background.draw(0, 0, ofGetWindowWidth(), ofGetWindowHeight());
		switch (gameState) {
		case LEVEL1: {
			boss1Emitter->draw();
			string bossLifeStr;
			bossLifeStr += std::to_string(boss1Emitter->life);
			ofSetColor(ofColor::red);
			font1.drawString(bossLifeStr, boss1Emitter->trans.x, boss1Emitter->trans.y - boss1Emitter->width / 2 - 30);
			if (boss1Emitter->phase == PHASE2) {
				string helper;
				helper += "LOOK OUT FROM BELOW";
				font1.drawString(helper, ofGetWindowWidth() / 2 - font1.stringWidth(helper)/2, 80);
			}
			ofSetColor(255, 255, 255, 255);
			break;
		}
		case LEVEL2: {
			boss2Emitter->draw();
			for (int i = 0; i < level2Emitters.size(); i++) {
				level2Emitters[i]->draw();
			}
			string bossLifeStr;
			bossLifeStr += std::to_string(boss2Emitter->life);
			ofSetColor(ofColor::red);
			font1.drawString(bossLifeStr, boss2Emitter->trans.x, boss2Emitter->trans.y - boss2Emitter->width / 2 - 30);
			if (boss2Emitter->phase == PHASE3) {
				string helper;
				helper += "LOOK OUT FROM ABOVE";
				font1.drawString(helper, ofGetWindowWidth() / 2 - font1.stringWidth(helper) / 2, 80);
			}
			ofSetColor(255, 255, 255, 255);
			break;
		}
		case LEVEL3: {
			boss3Emitter->draw();
			for (int i = 0; i < topL3Emitters.size(); i++) {
				topL3Emitters[i]->draw();
			}
			for (int i = 0; i < leftL3Emitters.size(); i++) {
				leftL3Emitters[i]->draw();
			}
			for (int i = 0; i < rightL3Emitters.size(); i++) {
				rightL3Emitters[i]->draw();
			}
			string bossLifeStr;
			bossLifeStr += std::to_string(boss3Emitter->life);
			ofSetColor(ofColor::red);
			font1.drawString(bossLifeStr, boss3Emitter->trans.x, boss3Emitter->trans.y - boss3Emitter->width / 2 - 30);
			if (boss3Emitter->phase == PHASE2) {
				string helper;
				helper += "IT'S INVINCIBLE WHILE TIME IS STOPPED";
				font1.drawString(helper, ofGetWindowWidth() / 2 - font1.stringWidth(helper) / 2, 80);
			}
			else if (boss3Emitter->phase == PHASE12) {
				string helper;
				helper += "DON'T LET IT REACH THE LEFT";
				string helper2;
				helper2 += "JUMP AND PUSH IT BACK WITH SHORT RANGED ATTACKS";
				font1.drawString(helper, ofGetWindowWidth() / 2 - font1.stringWidth(helper) / 2, 90);
				font1.drawString(helper2, ofGetWindowWidth() / 2 - font1.stringWidth(helper2) / 2, 180);
			}
			ofSetColor(255, 255, 255, 255);
			break;
		}
		}
		/*if (!bHide) {
			gui.draw();
		}*/
		emitter->draw();
		string scoreStr;
		scoreStr += "Score: " + std::to_string(score);
		ofSetColor(ofColor::white);
		font1.drawString(scoreStr, 0, 30);
		string levelStr;
		levelStr += "Level: " + std::to_string(level);
		font1.drawString(levelStr, (ofGetWindowWidth() / 2) - font1.stringWidth(levelStr) / 2,font1.stringHeight(levelStr));
		string lifeStr;
		lifeStr += "Life: " + std::to_string(emitter->life);
		ofSetColor(ofColor::green);
		font1.drawString(lifeStr, 0, ofGetWindowHeight() - 5);

	}
	else {
		// When the game isn't started, there will be some form of menu screen
		// The screens display information and give options for the player to select
		string scoreStr;
		scoreStr += "Score: " + std::to_string(score);
		ofSetColor(ofColor::white);
		switch (gameState) {
		case TITLE: {
			title.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			break;
		}
		case TUTORIAL1: {
			tutorial1.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			break;
		}
		case TUTORIAL2: {
			tutorial2.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			break;
		}
		case TUTORIAL3: {
			tutorial3.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			break;
		}
		case MENU :{
			menu.draw(0,0,ofGetWindowWidth(), ofGetHeight());
			break;
		}
		case GAMEOVER: {
			gameOver.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			font2.drawString(scoreStr, (ofGetWindowWidth() / 2) - font2.stringWidth(scoreStr)/2, ofGetWindowHeight() / 2 + font2.stringHeight(scoreStr) / 2);
			break;
		}
		case LEVEL1CLEAR: {
			level1Clear.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			font2.drawString(scoreStr, (ofGetWindowWidth() / 2) - font2.stringWidth(scoreStr) / 2, ofGetWindowHeight() / 2 + font2.stringHeight(scoreStr) / 2);
			break;
		}
		case LEVEL2CLEAR: {
			level2Clear.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			font2.drawString(scoreStr, (ofGetWindowWidth() / 2) - font2.stringWidth(scoreStr) / 2, ofGetWindowHeight() / 2 + font2.stringHeight(scoreStr) / 2);
			break;
		}
		case LEVEL3CLEAR: {
			background.draw(0, 0, ofGetWindowWidth(), ofGetHeight());
			endEmitter1->draw();
			endEmitter2->draw();
			ofSetColor(ofColor::green);
			string clearStr;
			clearStr += "CONGRATULATIONS!\nYOU BEAT THE GAME";
			font2.drawString(clearStr, (ofGetWindowWidth() / 2) - font2.stringWidth(clearStr) / 2, ofGetWindowHeight() / 6 - font2.stringHeight(clearStr) / 2);
			string returnStr;
			returnStr += "Press Enter to Return to Main Menu";
			font1.drawString(returnStr, (ofGetWindowWidth() / 2) - font1.stringWidth(returnStr) / 2,ofGetWindowHeight());
			font2.drawString(scoreStr, (ofGetWindowWidth() / 2) - font2.stringWidth(scoreStr) / 2, ofGetWindowHeight() / 3 + font2.stringHeight(scoreStr) / 2);
			ofSetColor(ofColor::white);
			string creditsStr;
			creditsStr += "Credits:";
			string creditsStr2;
			creditsStr2 += "Programming/Sprites: Anhduy Khong";
			string creditsStr3;
			creditsStr3 += "Sound Effects: soundbible.com";
			string creditsStr4;
			creditsStr4 += "Music: Youtube Audio Library";
			font1.drawString(creditsStr, (ofGetWindowWidth() / 2) - font1.stringWidth(creditsStr) / 2, ofGetWindowHeight()/2);
			font1.drawString(creditsStr2, (ofGetWindowWidth() / 2) - font1.stringWidth(creditsStr2) / 2, ofGetWindowHeight()/2 + 50);
			font1.drawString(creditsStr3, (ofGetWindowWidth() / 2) - font1.stringWidth(creditsStr3) / 2, ofGetWindowHeight()/2 + 100);
			font1.drawString(creditsStr4, (ofGetWindowWidth() / 2) - font1.stringWidth(creditsStr4) / 2, ofGetWindowHeight()/2 + 150);
			break;
		}
		}
	}
	// Draws frame rate
	string str;
	str += "Frame Rate: " + std::to_string(ofGetFrameRate());
	ofSetColor(ofColor::white);
	ofDrawBitmapString(str, ofGetWindowWidth() - 170, 15);
	ofSetColor(255, 255, 255, 255);
}
//--------------------------------------------------------------

//--------------------------------------------------------------
void ofApp::mouseMoved(int x, int y ){

}

//--------------------------------------------------------------
void ofApp::mouseDragged(int x, int y, int button){
	
}

//--------------------------------------------------------------
void ofApp::mousePressed(int x, int y, int button){
	
}

//--------------------------------------------------------------
void ofApp::mouseReleased(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mouseEntered(int x, int y){

}

//--------------------------------------------------------------
void ofApp::mouseExited(int x, int y){

}

void ofApp::keyPressed(int key) {
	switch (key) {
	// Numerical keys 1, 2, and 3 are used for stage selection
	case '1': {
		if (gameState == MENU) {
			start = true;
			setLevel1();
		}
		break;
	}
	case '2': {
		if (gameState == MENU) {
			start = true;
			setLevel2();
		}
		break;
	}
	case '3': {
		if (gameState == MENU) {
			start = true;
			setLevel3();
		}
		break;
	}
	case 'F':
	case 'f':
		ofToggleFullscreen();
		break;
	case 'H':
	case 'h':
		bHide = !bHide;
		break;
	// T at the main menu displays the tutorial
	case 't':
	case 'T': {
		if (gameState == MENU) {
			gameState = TUTORIAL1;
		}
		break;
	}
	// Z Key while the game is started shoots projectile attack
	case 'z': 
	case 'Z':{
		if (start && !bZKeyDown) {
			emitter->setBasicFire(true);
			bZKeyDown = true;
		}
		break;
	}
	// X Key while the game is started is a short ranged attacks
	case 'x':
	case 'X':{
		if (start && !bXKeyDown) {
			emitter->setSecondaryFire(true);
			bXKeyDown = true;
		}
		break;
		}
	case OF_KEY_ALT:
		break;
	case OF_KEY_CONTROL: {
		break;
	}
	case OF_KEY_SHIFT: {
		break;
	}
	// Jump
	case ' ': {
		if (!bSpaceKeyDown && !emitter->doubleJumping && start) {
			if (emitter->jumping) {
				emitter->doubleJumping = true;
			}
			emitter->velocity.y = -30;
			bSpaceKeyDown = true;
			emitter->jumping = true;
		}
		break;
	}
	// Jump
	case OF_KEY_UP: {
		if (!bUpKeyDown && !emitter->doubleJumping && start) {
			if (emitter->jumping) {
				emitter->doubleJumping = true;
			}
			emitter->velocity.y = -30;
			bUpKeyDown = true;
			emitter->jumping = true;
		}
		break;
	}
	// Fastfall
	case OF_KEY_DOWN: {
		if (start) {
			bDownKeyDown = true;
			emitter->velocity.y = 30;
		}
		break;
	}
	// Move left
	case OF_KEY_LEFT: {
		bLeftKeyDown = true;
		if (emitter->state != LEFT) {
			emitter->setLeft();
		}
		break;
	}
	//  Move right
	case OF_KEY_RIGHT: {
		bRightKeyDown = true;
		if (emitter->state != RIGHT) {
			emitter->setRight();
		}
		break;
	}
	// Controls state changes and transitions
	case OF_KEY_RETURN: {
		if (!bReturnKeyDown) {
			switch (gameState) {
			case GAMEOVER: {
				gameState = MENU;
				restart();
				break;
			}
			case TITLE: {
				gameState = TUTORIAL1;
				break;
			}
			case TUTORIAL1: {
				gameState = TUTORIAL2;
				break;
			}
			case TUTORIAL2: {
				gameState = TUTORIAL3;
				break;
			}
			case TUTORIAL3: {
				gameState = MENU;
				break;
			}
			case MENU: {
				start = true;
				setLevel1();
				break;
			}
			case LEVEL1CLEAR: {
				start = true;
				setLevel2();
				break;
			}
			case LEVEL2CLEAR: {
				start = true;
				setLevel3();
				break;
			}
			case LEVEL3CLEAR: {
				gameState = MENU;
				restart();
				clearBgm.stop();
				menuBgm.play();
				break;
			}
			}
		}
		bReturnKeyDown = true;
		break;
	}
	// Quick reset if game is started or skip tutorial
	case OF_KEY_BACKSPACE: {
		if (gameState == TUTORIAL1 || gameState == TUTORIAL2 || gameState == TUTORIAL3) {
			gameState = MENU;
		}
		else if (start) {
			setGameOver();
		}
	}
		break;
	}
}


//--------------------------------------------------------------
void ofApp::keyReleased(int key) {
	switch (key) {
	// Set player to idle
	case OF_KEY_LEFT: {
		bLeftKeyDown = false;
		if (emitter->state != RIGHT) {
			emitter->setIdle();
		}
		break;
	}
	// Set player to idle
	case OF_KEY_RIGHT: {
		bRightKeyDown = false;
		if (emitter->state != LEFT) {
			emitter->setIdle();
		}
		break;
	}
	// Releasing jump ealier decreases vertical velocity
	case ' ': {
		bSpaceKeyDown = false;
		if (emitter->velocity.y < -15) {
			emitter->velocity.y = -15;
		}
		break;
	}
	// Releasing jump ealier decreases vertical velocity
	case OF_KEY_UP: {
		bUpKeyDown = false;
		if (emitter->velocity.y < -15) {
			emitter->velocity.y = -15;
		}
		break;
	}
	case OF_KEY_DOWN: {
		bDownKeyDown = false;
		break;
	}
	case OF_KEY_RETURN: {
		bReturnKeyDown = false;
		break;
	}
	// player stops shooting projectiles
	case 'z':
	case 'Z':{
		emitter->setBasicFire(false);
		bZKeyDown = false;
		break;
	}
	// player stops attacking with short range
	case 'x':
	case 'X': {
		emitter->setSecondaryFire(false);
		bXKeyDown = false;
	}
	}
}

//--------------------------------------------------------------
void ofApp::windowResized(int w, int h){

}

//--------------------------------------------------------------
void ofApp::gotMessage(ofMessage msg){

}

//--------------------------------------------------------------
void ofApp::dragEvent(ofDragInfo dragInfo){ 

}


void ofApp::checkCollisionsLevel1() {

	//Checking for collision between boss and player
	ofVec2f diff = emitter->trans - boss1Emitter->trans;
	float dist = diff.length();
	if (emitter->hitboxRadius + boss1Emitter->width / 2 >= dist && !emitter->invincible) {
		emitter->life--;
		emitter->playerHit();
		player.play();
		if (emitter->life <= 0) {
			setGameOver();
		}
		else {
			emitter->lastHit = ofGetElapsedTimeMillis();
		}
	}

	// Checking for collision between player projectiles and boss
	vector<AnimatableSprite>::iterator playerProjectileIterator = emitter->sys->sprites.begin();
	while (playerProjectileIterator != emitter->sys->sprites.end()) {
		diff = playerProjectileIterator->trans - boss1Emitter->trans;
		dist = diff.length();
		if (playerProjectileIterator->width / 2 + boss1Emitter->width / 2 >= dist) {
			boss1Emitter->life -= playerProjectileIterator->pointVal;
			playerProjectileIterator->lifespan = 0;
			if (playerProjectileIterator->pointVal == 6) {
				score += playerProjectileIterator->pointVal * 5;
			}
			else {
				score += playerProjectileIterator->pointVal;
			}
		}
		playerProjectileIterator++;
	}

	//Checking for collision between boss projectiles and player 
	vector<Emitter>::iterator bossEmitterIterator = boss1Emitter->subsys->emitters.begin();
	while (bossEmitterIterator != boss1Emitter->subsys->emitters.end() && gameState == LEVEL1) {
		diff = emitter->trans - bossEmitterIterator->trans;
		dist = diff.length();
		if (emitter->hitboxRadius + bossEmitterIterator->width / 2 >= dist && !emitter->invincible) {
			emitter->life--;
			emitter->playerHit();
			bossEmitterIterator->lifespan = 0;
			player.play();
			if (emitter->life <= 0) {
				setGameOver();
			}
			else {
				emitter->lastHit = ofGetElapsedTimeMillis();
			}
		}
		vector<AnimatableSprite>::iterator emitterProjectileIterator = bossEmitterIterator->sys->sprites.begin();
		while (emitterProjectileIterator != bossEmitterIterator->sys->sprites.end()) {
			diff = emitter->trans - emitterProjectileIterator->trans;
			dist = diff.length();
			if (emitter->hitboxRadius + emitterProjectileIterator->width / 2 >= dist && !emitter->invincible) {
				emitter->life--;
				emitter->playerHit();
				emitterProjectileIterator->lifespan = 0;
				player.play();
				if (emitter->life <= 0) {
					setGameOver();
				}
				else {
					emitter->lastHit = ofGetElapsedTimeMillis();
				}
			}
			emitterProjectileIterator++;
		}
		bossEmitterIterator++;
	}
}

void ofApp::checkCollisionsLevel2() {

	//Checking for collision between boss and player
	ofVec2f diff = emitter->trans - boss2Emitter->trans;
	float dist = diff.length();
	if (emitter->hitboxRadius + boss2Emitter->width / 2 >= dist && !emitter->invincible) {
		emitter->life--;
		emitter->playerHit();
		player.play();
		if (emitter->life <= 0) {
			setGameOver();
		}
		else {
			emitter->lastHit = ofGetElapsedTimeMillis();
		}
	}

	// Checking for collision between player projectiles and boss
	vector<AnimatableSprite>::iterator playerProjectileIterator = emitter->sys->sprites.begin();
	while (playerProjectileIterator != emitter->sys->sprites.end()) {
		diff = playerProjectileIterator->trans - boss2Emitter->trans;
		dist = diff.length();
		if (playerProjectileIterator->width / 2 + boss2Emitter->width / 2 >= dist) {
			boss2Emitter->life -= playerProjectileIterator->pointVal;
			playerProjectileIterator->lifespan = 0;
			if (playerProjectileIterator->pointVal == 6) {
				score += playerProjectileIterator->pointVal * 5;
			}
			else {
				score += playerProjectileIterator->pointVal;
			}
		}
		playerProjectileIterator++;
	}

	//Checking for collision between boss projectiles and player 
	vector<AnimatableSprite>::iterator bossSpriteIterator = boss2Emitter->sys->sprites.begin();
	while (bossSpriteIterator != boss2Emitter->sys->sprites.end()) {
		diff = emitter->trans - bossSpriteIterator->trans;
		dist = diff.length();
		if (emitter->hitboxRadius + bossSpriteIterator->width / 2 >= dist && !emitter->invincible) {
			emitter->life--;
			emitter->playerHit();
			bossSpriteIterator->lifespan = 0;
			player.play();
			if (emitter->life <= 0) {
				setGameOver();
			}
			else {
				emitter->lastHit = ofGetElapsedTimeMillis();
			}
		}
		bossSpriteIterator++;
	}

	// Checking for collision between secondary emitter particles and player
	for (int i = 0; i < level2Emitters.size(); i++) {
		vector <Particle>::iterator topSpawnerIterator = level2Emitters[i]->partsys->particles.begin();
		while (topSpawnerIterator != level2Emitters[i]->partsys->particles.end()) {
			diff = emitter->trans - topSpawnerIterator->position;
			dist = diff.length();
			if (emitter->hitboxRadius + topSpawnerIterator->sprite.width / 2 >= dist && !emitter->invincible) {
				emitter->life--;
				emitter->playerHit();
				topSpawnerIterator->lifespan = 0;
				player.play();
				if (emitter->life <= 0) {
					setGameOver();
				}
				else {
					emitter->lastHit = ofGetElapsedTimeMillis();
				}
			}
			topSpawnerIterator++;
		}
	}
}

void ofApp::checkCollisionsLevel3() {

	//Checking for collision between boss and player
	ofVec2f diff = emitter->trans - boss3Emitter->trans;
	float dist = diff.length();
	if (boss3Emitter->started) {
		if (emitter->hitboxRadius + boss3Emitter->width / 2 >= dist && !emitter->invincible) {
			emitter->life--;
			emitter->playerHit();
			player.play();
			if (emitter->life <= 0) {
				setGameOver();
			}
			else {
				emitter->lastHit = ofGetElapsedTimeMillis();
			}
		}
	}

	// Checking for collision between player projectiles and boss, it cannot be damaged while projectiles are frozen
	if (boss3Emitter->started) {
		vector<AnimatableSprite>::iterator playerProjectileIterator = emitter->sys->sprites.begin();
		while (playerProjectileIterator != emitter->sys->sprites.end()) {
			diff = playerProjectileIterator->trans - boss3Emitter->trans;
			dist = diff.length();
			if (playerProjectileIterator->width / 2 + boss3Emitter->width / 2 >= dist) {
				boss3Emitter->life -= playerProjectileIterator->pointVal;
				playerProjectileIterator->lifespan = 0;
				if (playerProjectileIterator->pointVal == 6) {
					score += playerProjectileIterator->pointVal * 5;
				}
				else {
					score += playerProjectileIterator->pointVal;
				}
			}
			playerProjectileIterator++;
		}
	}
	// Special case with phase 12, test collisions between player projectiles and the level 3 boss's last projectile
	if (boss3Emitter->phase == PHASE12) {
		vector<AnimatableSprite>::iterator playerProjectileIterator = emitter->sys->sprites.begin();
		while (playerProjectileIterator != emitter->sys->sprites.end()) {
			diff = playerProjectileIterator->trans - boss3Emitter->lastProjectile.trans;
			dist = diff.length();
			if (playerProjectileIterator->pointVal == 6 && playerProjectileIterator->width / 2 + boss3Emitter->lastProjectile.width / 2 >= dist) {
				boss3Emitter->lastProjectile.velocity.x += 4;
				playerProjectileIterator->lifespan = 0;
			}
			playerProjectileIterator++;
		}
	}

	//Checks collision between boss projectiles(sprites) and player
	vector<AnimatableSprite>::iterator bossSpriteIterator = boss3Emitter->sys->sprites.begin();
	while (bossSpriteIterator != boss3Emitter->sys->sprites.end()) {
		diff = emitter->trans - bossSpriteIterator->trans;
		dist = diff.length();
		if (emitter->hitboxRadius + bossSpriteIterator->width / 2 >= dist && !emitter->invincible) {
			emitter->life--;
			emitter->playerHit();
			bossSpriteIterator->lifespan = 0;
			player.play();
			if (emitter->life <= 0) {
				setGameOver();
			}
			else {
				emitter->lastHit = ofGetElapsedTimeMillis();
			}
		}
		bossSpriteIterator++;
	}

	// Checks collisions between boss projectiles(particles) and player
	vector<Particle>::iterator bossParticleIterator = boss3Emitter->partsys->particles.begin();
	while (bossParticleIterator != boss3Emitter->partsys->particles.end()) {
		diff = emitter->trans - bossParticleIterator->position;
		dist = diff.length();
		if (emitter->hitboxRadius + bossParticleIterator->sprite.width / 2 >= dist && !emitter->invincible) {
			emitter->life--;
			emitter->playerHit();
			bossParticleIterator->lifespan = 0;
			player.play();
			if (emitter->life <= 0) {
				setGameOver();
			}
			else {
				emitter->lastHit = ofGetElapsedTimeMillis();
			}
		}
		bossParticleIterator++;
	}

	//Checks collision between top emitter particles and player
	for (int i = 0; i < topL3Emitters.size(); i++) {
		vector <Particle>::iterator topSpawnerIterator = topL3Emitters[i]->partsys->particles.begin();
		while (topSpawnerIterator != topL3Emitters[i]->partsys->particles.end()) {
			diff = emitter->trans - topSpawnerIterator->position;
			dist = diff.length();
			if (emitter->hitboxRadius + topSpawnerIterator->sprite.width / 2 >= dist && !emitter->invincible) {
				emitter->life--;
				emitter->playerHit();
				topSpawnerIterator->lifespan = 0;
				player.play();
				if (emitter->life <= 0) {
					setGameOver();
				}
				else {
					emitter->lastHit = ofGetElapsedTimeMillis();
				}
			}
			topSpawnerIterator++;
		}
	}
	// Checks collisions between left emitter sprites and player
	for (int i = 0; i < leftL3Emitters.size(); i++) {
		vector <AnimatableSprite>::iterator leftSpawnerIterator = leftL3Emitters[i]->sys->sprites.begin();
		while (leftSpawnerIterator != leftL3Emitters[i]->sys->sprites.end()) {
			diff = emitter->trans - leftSpawnerIterator->trans;
			dist = diff.length();
			if (emitter->hitboxRadius + leftSpawnerIterator->width / 2 >= dist && !emitter->invincible) {
				emitter->life--;
				emitter->playerHit();
				leftSpawnerIterator->lifespan = 0;
				player.play();
				if (emitter->life <= 0) {
					setGameOver();
				}
				else {
					emitter->lastHit = ofGetElapsedTimeMillis();
				}
			}
			leftSpawnerIterator++;
		}
	}
	// Checks collision between right emitter sprites and player
	for (int i = 0; i < rightL3Emitters.size(); i++) {
		vector <AnimatableSprite>::iterator rightSpawnerIterator = rightL3Emitters[i]->sys->sprites.begin();
		while (rightSpawnerIterator != rightL3Emitters[i]->sys->sprites.end()) {
			diff = emitter->trans - rightSpawnerIterator->trans;
			dist = diff.length();
			if (emitter->hitboxRadius + rightSpawnerIterator->width / 2 >= dist && !emitter->invincible) {
				emitter->life--;
				emitter->playerHit();
				rightSpawnerIterator->lifespan = 0;
				player.play();
				if (emitter->life <= 0) {
					setGameOver();
				}
				else {
					emitter->lastHit = ofGetElapsedTimeMillis();
				}
			}
			rightSpawnerIterator++;
		}
	}
}

// Creates the emitters for level 2
void ofApp::generateLevel2Emitters(int numberOfSpawners, vector<Emitter*> &enemyEmitter) {
	for (int i = 0; i < numberOfSpawners; i++) {
		enemyEmitter.push_back(new Emitter(new SpriteSystem(), new ParticleSystem()));
		enemyEmitter[i]->generateAndAddParticle("images/boss2_projectile_sprite_sheet.png");
		enemyEmitter[i]->childLifespan = 2000;
		enemyEmitter[i]->childSpawnRate = 1.5;
		enemyEmitter[i]->particles[0].sprite.setAnim(75, 75, 1, 1, 150, 0, 1, enemyEmitter[i]->childLifespan);
		enemyEmitter[i]->partsys->addForce(gravityForce);
	}
}

// Creates the emitters for level 3
void ofApp::generateLevel3Emitters() {
	for (int i = 0; i < 2; i++) {
		leftL3Emitters.push_back(new Emitter(new SpriteSystem(), new ParticleSystem));
		leftL3Emitters[i]->generateSprite("images/boss3_projectile_sprite_sheet.png");
		leftL3Emitters[i]->childVelocity = ofVec3f(600, 0, 0);
		leftL3Emitters[i]->childSpawnRate = 2;
		leftL3Emitters[i]->childLifespan = 3000;
		leftL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 640, 0, 1, leftL3Emitters[i]->childLifespan);
	}
	level3Emitters.push_back(leftL3Emitters);
	for (int i = 0; i < 2; i++) {
		topL3Emitters.push_back(new Emitter(new SpriteSystem(), new ParticleSystem()));
		topL3Emitters[i]->generateAndAddParticle("images/boss3_projectile_sprite_sheet.png");
		topL3Emitters[i]->childLifespan = 2000;
		topL3Emitters[i]->childSpawnRate = 4;
		topL3Emitters[i]->particles[0].sprite.setAnim(80, 80, 1, 1, 480, 0, 1, topL3Emitters[i]->childLifespan);
		topL3Emitters[i]->partsys->addForce(gravityForce);
	}
	level3Emitters.push_back(topL3Emitters);
	for (int i = 0; i < 2; i++) {
		rightL3Emitters.push_back(new Emitter(new SpriteSystem(), new ParticleSystem));
		rightL3Emitters[i]->generateSprite("images/boss3_projectile_sprite_sheet.png");
		rightL3Emitters[i]->childVelocity = ofVec3f(-600, 0, 0);
		rightL3Emitters[i]->childSpawnRate = 2;
		rightL3Emitters[i]->childLifespan = 3000;
		rightL3Emitters[i]->childSprites[0].setAnim(80, 80, 1, 1, 640, 0, 1, rightL3Emitters[i]->childLifespan);
	}
	level3Emitters.push_back(rightL3Emitters);
}

//Updates the level 2 emitters
// They move left and right between the window width as they spawn particles
void ofApp::updateLevel2Emitters() {
	for (int i = 0; i < level2Emitters.size(); i++) {
		if (boss2Emitter->phase == PHASE3) {
			if (level2Emitters[i]->trans.x + level2Emitters[i]->width / 2 > ofGetWindowWidth()) {
				level2Emitters[i]->trans.x = ofGetWindowWidth() - level2Emitters[i]->width / 2;
				level2Emitters[i]->velocity.x = -600;
			}
			else if (level2Emitters[i]->trans.x - level2Emitters[i]->width / 2 < 0) {
				level2Emitters[i]->trans.x = level2Emitters[i]->width / 2;
				level2Emitters[i]->velocity.x = 600;
			}
		}
		level2Emitters[i]->update();
	}
}

//Updates the Level 3 top emitters
// They move left and right between the window width as they spawn particles
void ofApp::updateTopL3Emitters() {
	if (boss3Emitter->phase == PHASE7 || boss3Emitter->phase == PHASE8 || boss3Emitter->phase == PHASE11) {
		if (topL3Emitters[0]->trans.x - topL3Emitters[0]->width / 2 < 0) {
			topL3Emitters[0]->trans.x = topL3Emitters[0]->width / 2;
			topL3Emitters[0]->velocity.x = 300;
		}
		else if (topL3Emitters[0]->trans.x + topL3Emitters[0]->width / 2 > ofGetWindowWidth() / 2) {
			topL3Emitters[0]->trans.x = ofGetWindowWidth() / 2 - topL3Emitters[0]->width / 2;
			topL3Emitters[0]->velocity.x = -300;
		}
		if (topL3Emitters[1]->trans.x - topL3Emitters[1]->width / 2 < ofGetWindowWidth() / 2) {
			topL3Emitters[1]->trans.x = ofGetWindowWidth()/2 + topL3Emitters[1]->width / 2;
			topL3Emitters[1]->velocity.x = 300;
		}
		else if (topL3Emitters[1]->trans.x + topL3Emitters[1]->width / 2 > ofGetWindowWidth()) {
			topL3Emitters[1]->trans.x = ofGetWindowWidth() - topL3Emitters[1]->width / 2;
			topL3Emitters[1]->velocity.x = -300;
		}
	}
	for (int i = 0; i < topL3Emitters.size(); i++) {
		topL3Emitters[i]->update();
	}
}

//Updates the Level 3 left emitters
// They move up and down between the window height as they spawn particles
void ofApp::updateLeftL3Emitters() {
	for (int i = 0; i < leftL3Emitters.size(); i++) {
		if (boss3Emitter->phase == PHASE9 || boss3Emitter->phase == PHASE10 || boss3Emitter->phase == PHASE11) {
			if (leftL3Emitters[i]->trans.y - leftL3Emitters[i]->height / 2 < 0) {
				leftL3Emitters[i]->trans.y = leftL3Emitters[0]->height / 2;
				leftL3Emitters[i]->velocity.y = 300;
			}
			else if (leftL3Emitters[i]->trans.y + leftL3Emitters[0]->height / 2 > ofGetWindowHeight()) {
				leftL3Emitters[i]->trans.y = ofGetWindowHeight() - leftL3Emitters[i]->height / 2;
				leftL3Emitters[i]->velocity.y = -300;
			}
		}
		leftL3Emitters[i]->update();
	}
}

//Updates the Level 3 right emitters
// They move up and down between the window height as they spawn particles
void ofApp::updateRightL3Emitters() {
	for (int i = 0; i < rightL3Emitters.size(); i++) {
		if (boss3Emitter->phase == PHASE9 || boss3Emitter->phase == PHASE10 || boss3Emitter->phase == PHASE11) {
			if (rightL3Emitters[i]->trans.y - rightL3Emitters[i]->height / 2 < 0) {
				rightL3Emitters[i]->trans.y = rightL3Emitters[0]->height / 2;
				rightL3Emitters[i]->velocity.y = 300;
			}
			else if (rightL3Emitters[i]->trans.y + rightL3Emitters[0]->height / 2 > ofGetWindowHeight()) {
				rightL3Emitters[i]->trans.y = ofGetWindowHeight() - rightL3Emitters[i]->height / 2;
				rightL3Emitters[i]->velocity.y = -300;
			}
		}
		rightL3Emitters[i]->update();
	}
}

// Updates the boss emitter for level 1
// It moves left and right between the window width
void ofApp::updateBoss1Emitter() {
	if (boss1Emitter->trans.x + boss1Emitter->width / 2 >= ofGetWindowWidth()) {
		boss1Emitter->trans.x = ofGetWindowWidth() - boss1Emitter->width / 2;
		boss1Emitter->velocity.x = -200;
	}
	else if (boss1Emitter->trans.x - boss1Emitter->width / 2 <= 0) {
		boss1Emitter->trans.x = boss1Emitter->width / 2;
		boss1Emitter->velocity.x = 200;
	}
	boss1Emitter->update();
}

// Updates the boss emitter for level 2
// It moves left and right between the window width and jumps
void ofApp::updateBoss2Emitter() {
	if (boss2Emitter->trans.x + boss2Emitter->width / 2 >= ofGetWindowWidth()) {
		boss2Emitter->trans.x = ofGetWindowWidth() - boss2Emitter->width / 2;
		boss2Emitter->velocity.x = -200;
	}
	else if (boss2Emitter->trans.x - boss2Emitter->width / 2 <= 0) {
		boss2Emitter->trans.x = boss2Emitter->width / 2;
		boss2Emitter->velocity.x = 200;
	}
	boss2Emitter->checkJump();
	boss2Emitter->update();
}

// Updates the boss emitter for level 3
// The boss is stationary
void ofApp::updateBoss3Emitter() {
	switch (boss3Emitter->phase) {
	case PHASE1: { // Boss targets player with projectiles
		ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->trans.x, emitter->trans.y - boss3Emitter->trans.y, 0);
		newVel = newVel.getNormalized() * 700;
		boss3Emitter->childVelocity = newVel;
		break;
	}
	case PHASE2: { // Same pattern with phase 1 with projectile freezing, then projectiles retarget player
		ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->trans.x, emitter->trans.y - boss3Emitter->trans.y, 0);
		newVel = newVel.getNormalized() * 700;
		boss3Emitter->childVelocity = newVel;
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopInterval && boss3Emitter->started) {
			boss3Emitter->sprite.advanceFrame();
			boss3Emitter->draw();
			boss3Emitter->stop();
		}
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopDuration && !boss3Emitter->started) {
			boss3Emitter->start();
			for (int i = 0; i < boss3Emitter->sys->sprites.size(); i++) {
				ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->sys->sprites[i].trans.x, emitter->trans.y - boss3Emitter->sys->sprites[i].trans.y, 0);
				newVel = newVel.getNormalized() * 1000;
				boss3Emitter->sys->sprites[i].velocity = newVel;
				boss3Emitter->sys->sprites[i].birthtime = ofGetElapsedTimeMillis();
				boss3Emitter->sys->sprites[i].lifespan = 3000;
			}
		}
		break;
	}
	// Phase 3 is handled in the boss3Emitter class due to no special cases of velocity or particle manipulation that requires extraneous variables
	// Fountain effect of particles
	case PHASE4: {// Same pattern as phase 3 with projectile freezing, then projectiles retarget player
		ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->trans.x, emitter->trans.y - boss3Emitter->trans.y, 0);
		newVel = newVel.getNormalized() * 700;
		boss3Emitter->childVelocity = newVel;
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopInterval && boss3Emitter->started) {
			boss3Emitter->sprite.advanceFrame();
			boss3Emitter->draw();
			boss3Emitter->stop();
			boss3Emitter->partsys->removeForces();
		}
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopDuration && !boss3Emitter->started) {
			boss3Emitter->start();
			boss3Emitter->partsys->addForce(gravityForce);
			for (int i = 0; i < boss3Emitter->partsys->particles.size(); i++) {
				ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->partsys->particles[i].position.x, emitter->trans.y - boss3Emitter->partsys->particles[i].position.y, 0);
				newVel = newVel.getNormalized() * 1000;
				boss3Emitter->partsys->particles[i].velocity = newVel;
				boss3Emitter->partsys->particles[i].birthtime = ofGetElapsedTimeMillis();
				boss3Emitter->partsys->particles[i].lifespan = 1;
			}
		}
		break;
	}
	// Phase 5 is handled in the boss3Emitter class due to no special cases of velocity or particle manipulation that requires extraneous variables
	// Shoots projectiles left and right 
	case PHASE6: {// Same pattern as phase 5 with projectile freezing, then projectiles retarget player
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopInterval && boss3Emitter->started) {
			boss3Emitter->sprite.advanceFrame();
			boss3Emitter->draw();
			boss3Emitter->stop();
		}
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopDuration && !boss3Emitter->started) {
			boss3Emitter->start();
			for (int i = 0; i < boss3Emitter->sys->sprites.size(); i++) {
				ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->sys->sprites[i].trans.x, emitter->trans.y - boss3Emitter->sys->sprites[i].trans.y, 0);
				newVel = newVel.getNormalized() * 1000;
				boss3Emitter->sys->sprites[i].velocity = newVel;
				boss3Emitter->sys->sprites[i].birthtime = ofGetElapsedTimeMillis();
				boss3Emitter->sys->sprites[i].lifespan = 500;
			}
		}
		break;
	}
	case PHASE7: { // Same as phase 1
		ofVec3f newVel = ofVec3f(emitter->trans.x - boss3Emitter->trans.x, emitter->trans.y - boss3Emitter->trans.y, 0);
		newVel = newVel.getNormalized() * 700;
		boss3Emitter->childVelocity = newVel;
		break;
	}
	case PHASE8: { // Phase 7 with projectile freezing, then particles move inwards
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopInterval && boss3Emitter->started) {
			boss3Emitter->sprite.advanceFrame();
			boss3Emitter->draw();
			boss3Emitter->stop();
			for (int i = 0; i < topL3Emitters.size(); i++) {
				topL3Emitters[i]->started = false;
				topL3Emitters[i]->partsys->removeForces();
				for (int j = 0; j < topL3Emitters[i]->partsys->particles.size(); j++) {
					topL3Emitters[i]->partsys->particles[j].velocity = ofVec3f(0, 0, 0);
					topL3Emitters[i]->partsys->particles[j].lifespan = -1;
				}
			}
		}
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopDuration && !boss3Emitter->started) {
			boss3Emitter->start();
			for (int i = 0; i < boss3Emitter->sys->sprites.size(); i++) {
				boss3Emitter->sys->sprites[i].velocity = ofVec3f(0,-500,0);
				boss3Emitter->sys->sprites[i].birthtime = ofGetElapsedTimeMillis();
				boss3Emitter->sys->sprites[i].lifespan = 500;
			}
			for (int i = 0; i < topL3Emitters.size(); i++) {
				topL3Emitters[i]->partsys->addForce(gravityForce);
				topL3Emitters[i]->start();
			}

			for (int i = 0; i < topL3Emitters[0]->partsys->particles.size(); i++) {
				topL3Emitters[0]->partsys->particles[i].lifespan = 1;
				topL3Emitters[0]->partsys->particles[i].birthtime = ofGetElapsedTimeMillis();
				topL3Emitters[0]->partsys->particles[i].velocity = ofVec3f(300, 0, 0);
			}
			for (int i = 0; i < topL3Emitters[1]->partsys->particles.size(); i++) {
				topL3Emitters[1]->partsys->particles[i].lifespan = 1;
				topL3Emitters[1]->partsys->particles[i].birthtime = ofGetElapsedTimeMillis();
				topL3Emitters[1]->partsys->particles[i].velocity = ofVec3f(-300, 0, 0);
			}
		}
		break;
	}
	// Phase 9 is handled in the update left and right emitters method utilizes right and left emitters to shoot projectiles horizontally
	case PHASE10: { // Phase 9 with projectile freezing, then projectiles fall down
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopInterval && boss3Emitter->started) {
			boss3Emitter->sprite.advanceFrame();
			boss3Emitter->draw();
			boss3Emitter->stop();
			for (int j = 0; j < leftL3Emitters.size(); j++) {
				leftL3Emitters[j]->stop();
				for (int i = 0; i < leftL3Emitters[j]->sys->sprites.size(); i++) {
					leftL3Emitters[j]->sys->sprites[i].velocity = ofVec3f(0, 0, 0);
					leftL3Emitters[j]->sys->sprites[i].lifespan = -1;
				}
			}
			for (int j = 0; j < rightL3Emitters.size(); j++) {
				rightL3Emitters[j]->stop();
				for (int i = 0; i < rightL3Emitters[j]->sys->sprites.size(); i++) {
					rightL3Emitters[j]->sys->sprites[i].velocity = ofVec3f(0, 0, 0);
					rightL3Emitters[j]->sys->sprites[i].lifespan = -1;
				}
			}
		}
		if (ofGetElapsedTimeMillis() - boss3Emitter->lastSwapped > boss3Emitter->timeStopDuration && !boss3Emitter->started) {
			boss3Emitter->start();
			for (int j = 0; j < leftL3Emitters.size(); j++) {
				leftL3Emitters[j]->start();
				for (int i = 0; i < leftL3Emitters[j]->sys->sprites.size(); i++) {
					leftL3Emitters[j]->sys->sprites[i].velocity = ofVec3f(0, 1000, 0);
					leftL3Emitters[j]->sys->sprites[i].birthtime = ofGetElapsedTimeMillis();
					leftL3Emitters[j]->sys->sprites[i].lifespan = 1000;
				}
			}
			for (int j = 0; j < rightL3Emitters.size(); j++) {
				rightL3Emitters[j]->start();
				for (int i = 0; i < rightL3Emitters[j]->sys->sprites.size(); i++) {
					rightL3Emitters[j]->sys->sprites[i].velocity = ofVec3f(0, 1000, 0);
					rightL3Emitters[j]->sys->sprites[i].birthtime = ofGetElapsedTimeMillis();
					rightL3Emitters[j]->sys->sprites[i].lifespan = 1000;
				}
			}
		}
		break;
	}
	// Phase 11 is handled in update where all secondary emitters are started
	case PHASE12: { // Spawn last projectile, if the last projectile reaches the left side set game over
		boss3Emitter->updateLastProjectile();
		if (boss3Emitter->lastProjectile.trans.x + boss3Emitter->lastProjectile.width < 0) {
			setGameOver();
		}
		break;
	}
	}
	boss3Emitter->update();
}

// Update the end emitters for fireworks effect
void ofApp::updateEndEmitters() {
		endEmitter1->update();
		endEmitter2->update();
}

// Set gamestate to level 1 and start the player and boss
void ofApp::setLevel1() {
	gameState = LEVEL1;
	level = 1;
	emitter->start();
	boss1Emitter->start();
	menuBgm.stop();
	level1Bgm.play();
}

// Set the gamestate to level 2 and start the player and boss
void ofApp::setLevel2() {
	gameState = LEVEL2;
	level = 2;
	emitter->start();
	boss2Emitter->start();
	menuBgm.stop();
	level1Bgm.stop();
	level2Bgm.play();
}

// Set the gamestate to level 3 and start the player and boss
// Player starts on left side of screen this time since boss spawns in the center
void ofApp::setLevel3(){
	gameState = LEVEL3;
	level = 3;
	emitter->setPosition(ofVec3f(emitter->width / 2, ofGetWindowHeight() - emitter->height / 2, 0));
	emitter->start();
	boss3Emitter->start();
	menuBgm.stop();
	level2Bgm.stop();
	level3Bgm.play();
}

// Set gamestate to level 1 clear
// Calculate score
// Regain life equal to level value
void ofApp::setLevel1Clear() {
	gameState = LEVEL1CLEAR;
	score += emitter->life * 100;
	emitter->life += level;
	emitter->reset();
	stop();
	menuBgm.play();
}

// Set gamestate to level 2 clear
// Calculate score
// Regain life equal to level value
void ofApp::setLevel2Clear() {
	gameState = LEVEL2CLEAR;
	score += emitter->life * 100;
	emitter->life += level;
	emitter->reset();
	stop();
	menuBgm.play();
}

// Set gamestate to level 3 clear
// Calculate score
// Start end emitters
void ofApp::setLevel3Clear() {
	gameState = LEVEL3CLEAR;
	score += emitter->life * 100;
	endEmitter1->start();
	endEmitter2->start();
	stop();
	clearBgm.play();

}

// Set gamestate to gameover and stops the game
void ofApp::setGameOver() {
	gameState = GAMEOVER;
	stop();
	menuBgm.play();
}

// Stops the current game
// Stops all emitters
void ofApp::stop(){
	start = false;
	level1Bgm.stop();
	level2Bgm.stop();
	level3Bgm.stop();
	boss1Emitter->stop();
	boss2Emitter->Emitter::stop();
	boss3Emitter->Emitter::stop();
	for (int i = 0; i < level2Emitters.size(); i++) {
		level2Emitters[i]->stop();
	}
	for (int j = 0; j < level3Emitters.size(); j++) {
		for (int i = 0; i < level3Emitters[j].size(); i++) {
			level3Emitters[j][i]->velocity = ofVec3f(0, 0, 0);
			level3Emitters[j][i]->stop();
		}
	}
}

// Resets values of the game
// Reset positions of emitters
// Reset score
// Reset level
// Reset values of all emitters
void ofApp::restart() {
    emitter->setPosition(ofVec3f(ofGetWindowWidth() / 2, ofGetWindowHeight() - emitter->height / 2, 0));
	level2Emitters[0]->setPosition(ofVec3f(level2Emitters[0]->width / 2, -level2Emitters[0]->height / 2, 0));
	level2Emitters[1]->setPosition(ofVec3f(ofGetWindowWidth() - level2Emitters[1]->width / 2, -level2Emitters[1]->height/2, 0));
	topL3Emitters[0]->setPosition(ofVec3f(topL3Emitters[0]->width / 2, -topL3Emitters[0]->height/2, 0));
	topL3Emitters[1]->setPosition(ofVec3f(ofGetWindowWidth() / 2 + topL3Emitters[1]->width / 2, -topL3Emitters[1]->height/2, 0));
	leftL3Emitters[0]->setPosition(ofVec3f(-leftL3Emitters[0]->width/2, leftL3Emitters[0]->height / 2, 0));
	leftL3Emitters[1]->setPosition(ofVec3f(-leftL3Emitters[1]->width/2, ofGetWindowHeight() - leftL3Emitters[1]->height / 2, 0));
	rightL3Emitters[0]->setPosition(ofVec3f(ofGetWindowWidth() + rightL3Emitters[0]->width / 2, rightL3Emitters[0]->height / 2, 0));
	rightL3Emitters[1]->setPosition(ofVec3f(ofGetWindowWidth() + rightL3Emitters[1]->width / 2, ofGetWindowHeight() - rightL3Emitters[1]->height / 2, 0));
	score = 0;
	level = 1;
	emitter->life = emitter->maxLife;
	boss1Emitter->reset();
	boss2Emitter->reset();
	boss3Emitter->reset();
	endEmitter1->stop();
	endEmitter2->stop();
	emitter->reset();
}