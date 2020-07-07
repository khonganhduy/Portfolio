#pragma once
//  Kevin M. Smith - CS 134 SJSU
// Modifications made by Anhduy Khong
// 3/31/19

#include "ofMain.h"
#include "Particle.h"


//  Pure Virtual Function Class - must be subclassed to create new forces.
//

// Force on particle
class ParticleForce {
protected:
public:
	bool applyOnce = false;
	bool applied = false;
	virtual void updateForce(Particle *) = 0;
};

// Particle system for managing particles
class ParticleSystem {
public:
	void add(const Particle &);
	void addForce(ParticleForce *);
	void removeForces() { forces.clear(); }
	void remove(int);
	void update();
	void setLifespan(float);
	void reset();
	int removeNear(const ofVec3f & point, float dist);
	void draw();
	vector<Particle> particles;
	vector<ParticleForce *> forces;
};



// Some convenient built-in forces
//
class GravityForce: public ParticleForce {
	ofVec3f gravity;
public:
	void set(ofVec3f g) { gravity = g; };
	GravityForce(const ofVec3f & gravity);
	void updateForce(Particle *);
};

class TurbulenceForce : public ParticleForce {
	ofVec3f tmin, tmax;
public:
	TurbulenceForce(const ofVec3f & min, const ofVec3f &max);
	void updateForce(Particle *);
};

class ImpulseRadialForce : public ParticleForce {
	float magnitude;
	float height;
public:
	ImpulseRadialForce(float magnitude); 
	void updateForce(Particle *);
	void setHeight(float);
	void setMagnitude(float);
};

class CyclicForce : public ParticleForce {
	float magnitude;

public:
	CyclicForce(float);
	void updateForce(Particle* particle);
};
