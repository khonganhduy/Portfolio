#pragma once
//----------------------------------------------------------------------------------
//
// Basic Sprite Emitter Class
// Original provided by Kevin Smith
//
//
//  Kevin M. Smith - CS 134 SJSU
// Modifications made by Anhduy Khong
// 3/31/19
//--------------------------------------------------------------

#include "Sprite.h"
#include "States.h"
#include "ParticleSystem.h"
#include "Particle.h"


//  General purpose Emitter class for emitting sprites
//  This works similar to a Particle emitter
//
class Emitter : public BaseObject {
public:
	Emitter() {};
	Emitter(SpriteSystem*, ParticleSystem*);
	void draw();
	void start();
	void stop();
	void setChildLifeSpan(float);
	void setChildVelocity(ofVec3f);
	void setChildSize(float w, float h) { childWidth = w; childHeight = h; }
	void setImage(ofImage);
	void setRate(float);
	void setSize(Sprite);
	float emitterAge();
	float maxDistPerFrame();
	void update();
	SpriteSystem *sys;
	ParticleSystem* partsys;
	float childSpawnRate;
	ofVec3f childVelocity;
	ofVec3f velocity;
	float childLifespan;
	float lifespan;
	bool started;
	float lastSpawned;
	float birthTime;
	ofImage childImage;
	ofImage image;
	ofSoundPlayer player;
	AnimatableSprite sprite;
	bool drawable;
	bool haveChildImage;
	bool haveImage;
	float width, height;
	float childWidth, childHeight;
	int groupSize;
	vector<AnimatableSprite> childSprites;
	vector<Particle> particles;

	void setPosition(ofVec3f);
	void generateAndAddParticle(string);
	void generateSprite(string path);
	void generateSprite(string path, string soundPath, int pointValue);
};

// Emitter system to manage possible sub emitters
class EmitterSystem {
public:
	void add(Emitter);
	void remove(int);
	void update();
	void removeNear(ofVec3f point, float dist);
	void draw();
	vector<Emitter> emitters;
};

// Player Emitter
class PlayerEmitter : public Emitter {
public:
	PlayerEmitter() {};
	PlayerEmitter(SpriteSystem*, ParticleSystem*, string);

	void setBasicFire(bool bFire);
	void setSecondaryFire(bool sFire);
	void setRight();
	void setLeft();
	void setIdle();
	void draw();
	void update();
	void playerHit();
	void reset();
	bool basicFire;
	bool secondaryFire;
	bool jumping;
	bool doubleJumping;
	bool invincible;
	bool rotateRight;
	double gravity = 1.5;
	double lastHit;
	double invincibilityTime = 2000;
	float hitboxRadius;
	int maxLife;
	int life;
	PlayerAnimState state = IDLE;
};

// Boss 1 Emitter
class Boss1Emitter : public Emitter {
public:
	Boss1Emitter() {};
	Boss1Emitter(SpriteSystem* spriteSystem, ParticleSystem* particleSystem, EmitterSystem* emitterSystem, string spritePath);
	void generateSubEmitters(string subImagePath);
	void update();
	void draw();
	void stop();
	void setPhase1();
	void setPhase2();
	void reset();
	vector<Emitter> subEmitters;
	EmitterSystem* subsys;
	int maxLife;
	int life;
	BossState phase;
};

// Boss 2 Emitter
class Boss2Emitter : public Emitter {
public:
	Boss2Emitter() {};
	Boss2Emitter(SpriteSystem* spriteSystem, ParticleSystem* particleSystem, string spritePath);
	void update();
	void draw();
	void setPhase1();
	void setPhase2();
	void setPhase3();
	void checkJump();
	void reset();
	int maxLife;
	int life;
	float gravity;
	bool jumping;
	bool swap;
	int counter;
	BossState phase;
};

// Boss 3 Emitter
class Boss3Emitter : public Emitter {
public:
	Boss3Emitter() {};
	Boss3Emitter(SpriteSystem* spriteSystem, ParticleSystem* particleSystem, string spritePath);
	void update();
	void updateLastProjectile();
	void draw();
	void start();
	void stop();
	void setPhase1();
	void setPhase2();
	void setPhase3();
	void setPhase4();
	void setPhase5();
	void setPhase6();
	void setPhase7();
	void setPhase8();
	void setPhase9();
	void setPhase10();
	void setPhase11();
	void setPhase12();
	void reset();
	Sprite lastProjectile;
	int maxLife;
	int life;
	int lastSwapped;
	int timeStopInterval;
	int timeStopDuration;
	int counter;
	BossState phase;
};