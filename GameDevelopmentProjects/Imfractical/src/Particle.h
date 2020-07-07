#pragma once


// Basic particle class
// Original provided by Kevin Smith CS134 SJSU
// Modifications made by Anhduy Khong
// 3/31/19


#include "ofMain.h"
#include "Sprite.h"

class ParticleForceField;

// Particle class, has a sprite
class Particle {
public:
	Particle();

	ofVec3f position;
	ofVec3f velocity;
	ofVec3f acceleration;
	ofVec3f forces;
	float	damping;
	float   mass;
	float   lifespan;
	float   radius;
	float   birthtime;
	void    integrate();
	void    draw();
	float   age();        // sec
	void update();
	void setPosition(ofVec3f);
	AnimatableSprite sprite;
};


