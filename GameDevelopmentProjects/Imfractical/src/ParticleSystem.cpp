
// Kevin M.Smith - CS 134 SJSU
// Modifications made by Anhduy Khong
// 3/31/19

#include "ParticleSystem.h"

//Adds particle to the particle system
void ParticleSystem::add(const Particle &p) {
	particles.push_back(p);
}

// Adds a force tot he particle system
void ParticleSystem::addForce(ParticleForce *f) {
	forces.push_back(f);
}

// Removes a specific particle given the index
void ParticleSystem::remove(int i) {
	particles.erase(particles.begin() + i);
}

// Sets the lifespan of particles
void ParticleSystem::setLifespan(float l) {
	for (int i = 0; i < particles.size(); i++) {
		particles[i].lifespan = l;
	}
}

// Resets the forces applied on particles if they are only applied once
void ParticleSystem::reset() {
	for (int i = 0; i < forces.size(); i++) {
		forces[i]->applied = false;
	}
}

// Updates the particle system
void ParticleSystem::update() {
	// check if empty and just return
	if (particles.size() == 0) return;

	vector<Particle>::iterator p = particles.begin();
	vector<Particle>::iterator tmp;

	// check which particles have exceed their lifespan and delete
	// from list.  When deleting multiple objects from a vector while
	// traversing at the same time, we need to use an iterator.
	//
	while (p != particles.end()) {
		if (p->lifespan != -1 && p->age() > p->lifespan) {
			tmp = particles.erase(p);
			p = tmp;
		}
		else p++;
	}

	// update forces on all particles first 
	//
	for (int i = 0; i < particles.size(); i++) {
		for (int k = 0; k < forces.size(); k++) {
			if (!forces[k]->applied)
				forces[k]->updateForce( &particles[i] );
		}
	}

	// update all forces only applied once to "applied"
	// so they are not applied again.
	//
	for (int i = 0; i < forces.size(); i++) {
		if (forces[i]->applyOnce)
			forces[i]->applied = true;
	}

	// integrate all the particles in the store
	//
	for (int i = 0; i < particles.size(); i++)
		particles[i].integrate();

}

// remove all particlies within "dist" of point (not implemented as yet)
//
int ParticleSystem::removeNear(const ofVec3f & point, float dist) { return 0; }

//  draw the particle cloud
//
void ParticleSystem::draw() {
	for (int i = 0; i < particles.size(); i++) {
		particles[i].update();
		particles[i].draw();
	}
}


// Gravity Force Field 
//
GravityForce::GravityForce(const ofVec3f &g) {
	gravity = g;
}

void GravityForce::updateForce(Particle * particle) {
	//
	// f = mg
	//
	particle->forces += gravity * particle->mass;
}

// Turbulence Force Field 
//
TurbulenceForce::TurbulenceForce(const ofVec3f &min, const ofVec3f &max) {
	tmin = min;
	tmax = max;
}

void TurbulenceForce::updateForce(Particle * particle) {
	//
	// We are going to add a little "noise" to a particles
	// forces to achieve a more natual look to the motion
	//
	particle->forces.x += ofRandom(tmin.x, tmax.x);
	particle->forces.y += ofRandom(tmin.y, tmax.y);
	particle->forces.z += ofRandom(tmin.z, tmax.z);
}

// Impulse Radial Force - this is a "one shot" force that
// eminates radially outward in random directions.
//
ImpulseRadialForce::ImpulseRadialForce(float magnitude) {
	this->magnitude = magnitude;
	height = 1;
	applyOnce = true;
}

void ImpulseRadialForce::updateForce(Particle * particle) {

	// we basically create a random direction for each particle
	// the force is only added once after it is triggered.
	//
	ofVec3f dir = ofVec3f(ofRandom(-1, 1), ofRandom(-height,height), ofRandom(-1, 1));
		particle->forces += dir.getNormalized() * magnitude;
}

void ImpulseRadialForce::setHeight(float newHeight) {
	height = newHeight;
}

void ImpulseRadialForce::setMagnitude(float newMag) {
	magnitude = newMag;
}

// Constant application of a cyclic force on a particle
CyclicForce::CyclicForce(float mag) {
	magnitude = mag;
}

// Gets the perpendicular vector of the particle using the Y axis as the other reference vector
// Adds to forces a vector that is perpendicular to the current vector
void CyclicForce::updateForce(Particle * particle) {
	float x = particle->velocity.x;
	float z = particle->velocity.z;
	ofVec3f dir = ofVec3f(x, 0, z);
	dir = dir.getNormalized();
	ofVec3f perp = dir.getPerpendicular(ofVec3f(0, 1, 0));
	perp = perp.getNormalized();
	particle->forces += perp * magnitude;
}
