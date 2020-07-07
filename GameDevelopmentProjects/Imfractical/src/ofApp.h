#pragma once

#include "ofMain.h"
#include "ofxGui.h"
#include "Sprite.h"
#include "Emitter.h"
#include "ParticleSystem.h"
//----------------------------------------------------------------------------------
//
// Emitter example source file provided by Kevin Smith
//
//  Kevin M. Smith - CS 134 SJSU
//  Modifications by Anhduy Khong
// 3/31/19
//--------------------------------------------------------------

//typedef enum { MoveStop, MoveLeft, MoveRight, MoveUp, MoveDown } MoveDir;



class Emitter;

class ofApp : public ofBaseApp{

	public:
		void setup();
		void update();
		void draw();
		void checkCollisionsLevel1();
		void checkCollisionsLevel2();
		void checkCollisionsLevel3();
		void generateLevel2Emitters(int, vector<Emitter*>&);
		void generateLevel3Emitters();
		void updateLevel2Emitters();
		void updateTopL3Emitters();
		void updateLeftL3Emitters();
		void updateRightL3Emitters();
		void updateBoss1Emitter();
		void updateBoss2Emitter();
		void updateBoss3Emitter();
		void updateEndEmitters();
		void setLevel1();
		void setLevel2();
		void setLevel3();
;		void setLevel1Clear();
		void setLevel2Clear();
		void setLevel3Clear();
		void setGameOver();
		void stop();
		void restart();

		void keyPressed(int key);
		void keyReleased(int key);
		void mouseMoved(int x, int y );
		void mouseDragged(int x, int y, int button);
		void mousePressed(int x, int y, int button);
		void mouseReleased(int x, int y, int button);
		void mouseEntered(int x, int y);
		void mouseExited(int x, int y);
		void windowResized(int w, int h);
		void dragEvent(ofDragInfo dragInfo);
		void gotMessage(ofMessage msg);
		
		GameState gameState;

		Emitter* endEmitter1;
		Emitter* endEmitter2;
		PlayerEmitter *emitter; 
		Boss1Emitter* boss1Emitter;
		Boss2Emitter* boss2Emitter;
		Boss3Emitter* boss3Emitter;
		vector<Emitter*> level2Emitters;
		vector<vector<Emitter*>> level3Emitters;
		vector<Emitter*> topL3Emitters;
		vector<Emitter*> rightL3Emitters;
		vector<Emitter*> leftL3Emitters;

		GravityForce* gravityForce;
		ImpulseRadialForce* impulseForce;

		ofSoundPlayer player;
		ofSoundPlayer menuBgm;
		ofSoundPlayer level1Bgm;
		ofSoundPlayer level2Bgm;
		ofSoundPlayer level3Bgm;
		ofSoundPlayer clearBgm;

		ofImage menu;
		ofImage background; // image for playing background
		ofImage title;
		ofImage tutorial1;
		ofImage tutorial2;
		ofImage tutorial3;
		ofImage level1Clear; // image for main menu
		ofImage level2Clear;
		ofImage gameOver;
		//ofVec3f mouse_last;

		bool bHide;
		bool bUpKeyDown;
		bool bDownKeyDown;
		bool bLeftKeyDown;
		bool bRightKeyDown;
		bool bSpaceKeyDown;
		bool bReturnKeyDown;
		bool bZKeyDown;
		bool bXKeyDown;

		bool start;
		int score;
		int level = 1;

		ofTrueTypeFont font1;
		ofTrueTypeFont font2;
		
		// Removed sliders for release version
		/*ofxFloatSlider rate;
		ofxFloatSlider life;
		ofxVec3Slider velocity;
		ofxLabel screenSize;

		ofxPanel gui;*/
};
