


#include "ofMain.h"
#include "Emitter.h"


//--------------------------------------------------------------
//  Create a new Emitter - needs a SpriteSystem and Particle System
//	Anhduy Khong CS134 - SJSU
//	3/31/19
//--------------------------------------------------------------

// Creates a new emitter with a sprite system and particle system
// This is so an emitter can have the option of emitting both sprites and particles
Emitter::Emitter(SpriteSystem* spriteSystem, ParticleSystem *particleSys) {
	sys = spriteSystem;
	partsys = particleSys;
	haveImage = false;
	groupSize = 1;
	childSpawnRate = 1;
	velocity = ofVec3f(0, 0, 0);
	childLifespan = 3000;    // milliseconds
	started = false;
	lastSpawned = 0;
	drawable = true;
	width = 50;
	height = 50;
	birthTime = ofGetElapsedTimeMillis();
}

//  Draw the Emitter if it is drawable. In many cases you would want a hidden emitter
//
//
void Emitter::draw() {
	if (drawable) {

		if (haveImage) {
			ofSetColor(255, 255, 255, 255); // Gives the emitter color
			sprite.draw();
		}
		// Debug block, commented out for release version
		/*else {
			width = 50;
			height = 50;
			ofSetColor(0, 0, 255);
			ofDrawRectangle(-width / 2 + trans.x, -height / 2 + trans.y, width, height);
			ofSetColor(255, 255, 255, 255);
		}*/
	}

	// draw sprite system
	//
	if (partsys->particles.size() > 0) {
		partsys->draw();
	}
	sys->draw();
}

//  Update the Emitter. If it has been started, spawn new sprites with
//  initial velocity, lifespan, birthtime.
//	Emitters are also capable of spawning particles
//	Update properties of emitter even if not started to despawn particles and sprites
void Emitter::update() {
	if (started) {

		float time = ofGetElapsedTimeMillis();
		// Tests to see if space bar is held to spawn a sprite (projectile)
		if (particles.size() > 0 && (time - lastSpawned) > (1000.0 / childSpawnRate)) {
			partsys->reset();
			for (int i = 0; i < groupSize; i++) {
				particles[0].lifespan = childLifespan / 1000;
				particles[0].setPosition(trans);
				particles[0].birthtime = time;
				partsys->add(particles[0]);
			}
			lastSpawned = time;
		}
		else if ((time - lastSpawned) > (1000.0 / childSpawnRate) && childSprites.size() != 0) {
			// spawn a new sprite
			childSprites[0].velocity = childVelocity;
			childSprites[0].lifespan = childLifespan;
			childSprites[0].trans = trans;
			childSprites[0].birthtime = time;
			sys->add(childSprites[0]);
			lastSpawned = time;

		}
	}
	trans += velocity / ofGetFrameRate();
	if (haveImage) {
		width = sprite.width;
		height = sprite.height;
	}
	sprite.setPosition(trans);
	sprite.update();
	sys->update();
	if (partsys->particles.size() > 0) {
		partsys->update();
	}
	
}

// Start/Stop the emitter.
//
void Emitter::start() {
	started = true;
	sprite.startAnim();
	lastSpawned = ofGetElapsedTimeMillis();
}

void Emitter::stop() {
	for (int i = 0; i < sys->sprites.size(); i++) {
		sys->sprites[i].lifespan = 0;
	}
	for (int i = 0; i < partsys->particles.size(); i++) {
		partsys->particles[i].lifespan = 0;
	}
	sprite.stopAnim();
	started = false;
}

// Sets childlifespan
void Emitter::setChildLifeSpan(float life) {
	childLifespan = life;
}

// Sets childVelocity
void Emitter::setChildVelocity(ofVec3f v) {
	childVelocity = v;
}


// Sets the image to an emitter
void Emitter::setImage(ofImage img) {
	image = img;
	sprite.setImage(img);
	haveImage = true;
}

// Sets the rate of the emitter
void Emitter::setRate(float r) {
	childSpawnRate = r;
}

// Sets the size of the mitter
void Emitter::setSize(Sprite sprite) {
	width = sprite.width;
	height = sprite.height;
}

// Sets the position of the emitter
void Emitter::setPosition(ofVec3f newPos) {
	trans = newPos;
	sprite.trans = newPos;
}

// Returns how far a child can travel in 1 frame
float Emitter::maxDistPerFrame() {
	return  childVelocity.length() / ofGetFrameRate();
}

// Creates sprites from a image path
// It adds it to the array of sprites so the emitter can spawn more than 1 type of sprite
void Emitter::generateSprite(string path) {
	AnimatableSprite aSprite;
	if (childImage.loadImage(path)) {
		aSprite.setImage(childImage);
		haveChildImage = true;
	}
	else {
		cout << "Cannot load sprite" << endl;
		ofExit();
	}
	aSprite.setPosition(trans);
	aSprite.width = childImage.getWidth();
	aSprite.height = childImage.getHeight();
	childSprites.push_back(aSprite);
}

// Creates sprites from an image path, sound path, and gives it a point value
// The point value is used to calculate score and deal damage to the boss
// It adds it to the array of sprites so the emitter can spawn more than 1 type of sprite
void Emitter::generateSprite(string path, string soundPath, int pointValue) {
	AnimatableSprite aSprite;
	if (childImage.loadImage(path)) {
		aSprite.setImage(childImage);
		haveChildImage = true;
	}
	else {
		cout << "Cannot load sprite" << endl;
		ofExit();
	}
	if (!aSprite.player.load(soundPath)) {
		cout << "Cannot load sound file" << endl;
		ofExit();
	}
	aSprite.setPosition(trans);
	aSprite.width = childImage.getWidth();
	aSprite.height = childImage.getHeight();
	aSprite.pointVal = pointValue;
	childSprites.push_back(aSprite);
}

// Generates particle from an image path
// Adds the particle to the array of particles so an emitter can spawn more than 1 type of particle
void Emitter::generateAndAddParticle(string imagePath) {
	Particle particle;
	if (image.loadImage(imagePath)) {
		particle.sprite.setImage(image);
	}
	else {
		cout << "Could not load particle image path from " << imagePath << endl;
		ofExit();
	}
	particles.push_back(particle);
}

float Emitter::emitterAge() {
	return (ofGetElapsedTimeMillis() - birthTime);
}

//  Add a Emitter to the Emitter System
//

void EmitterSystem::add(Emitter e) {
	emitters.push_back(e);
}

// Remove a emitter from the emitter system. Note that this function is not currently
// used. The typical case is that emitter automatically get removed when the reach
// their lifespan.
//
void EmitterSystem::remove(int i) {
	emitters.erase(emitters.begin() + i);
}

// remove all emitters within a given dist of point
//
void EmitterSystem::removeNear(ofVec3f point, float dist) {
	vector<Emitter>::iterator e = emitters.begin();
	vector<Emitter>::iterator tmp;

	while (e != emitters.end()) {
		ofVec3f v = e->trans - point;
		if (v.length() < dist) {
			tmp = emitters.erase(e);
			e = tmp;
		}
		else e++;
	}
}


//  Update the EmitterSystem by checking which emitter have exceeded their
//  lifespan (and deleting).  Also the emitter is moved to it's next
//  location based on velocity and direction.
//
void EmitterSystem::update() {

	if (emitters.size() == 0) return;
	vector<Emitter>::iterator e = emitters.begin();
	vector<Emitter>::iterator tmp;

	// check which emitter have exceed their lifespan and delete
	// from list.  When deleting multiple objects from a vector while
	// traversing at the same time, use an iterator.
	//
	while (e != emitters.end()) {
		if (e->lifespan != -1 && e->emitterAge() > e->lifespan) {
			tmp = emitters.erase(e);
			e = tmp;
		}
		else e++;
	}

	//  Move emitter
	//
	for (int i = 0; i < emitters.size(); i++) {
		emitters[i].update();
	}
}

//  Render all the emitters
//
void EmitterSystem::draw() {
	for (int i = 0; i < emitters.size(); i++) {
		emitters[i].draw();
	}
}

// Creates a player emitter which is a sub class of an emitter
// Initializes starting values of the player emitter
// Generates sprites that the player will emit
// Sets the state to idle
PlayerEmitter::PlayerEmitter(SpriteSystem* spriteSystem, ParticleSystem *particleSystem, string spritePath) :Emitter(spriteSystem, particleSystem) {
	childVelocity = ofVec3f(2000, 0, 0); // initialize child particle velocity
	childLifespan = 1000; // initliaze child lifespan
	childSpawnRate = 10;
	maxLife = 5;
	life = maxLife;
	groupSize = 30;
	invincible = false;
	rotateRight = true;
	basicFire = false;
	secondaryFire = false;
	jumping = false;
	doubleJumping = false;
	if (image.loadImage(spritePath)) {
		sprite.setImage(image);
		hitboxRadius = width * .25;
		haveImage = true;
	}
	else {
		haveImage = false;
		cout << "could not load sprite" << endl;
		ofExit();
	}
	generateSprite("images/player_projectile_sprite_sheet.png", "sfx/laser.ogg",2);
	generateSprite("images/player_projectile_sprite_sheet.png", "sfx/sword.wav", 6);
	childSprites[0].setAnim(22, 21, 1, 1, 0, 0, 1, childLifespan);
	childSprites[1].setAnim(125, 125, 1, 1, 21, 0, 1, 1000);
	setIdle();
}

// Sets the state of basic fire
void PlayerEmitter::setBasicFire(bool bFire) {
	basicFire = bFire;
}

// Sets the state of secondary fire
void PlayerEmitter::setSecondaryFire(bool sFire) {
	secondaryFire = sFire;
}

//Sets the state of the player to facing right
// Set the animation accordingly depending on if the player is invincible
// Set the velocity to move the player
void PlayerEmitter::setRight() {
	rotateRight = true;
	velocity.x = 15;
	childVelocity.x = 2000;
	if (!invincible) {
		sprite.setAnim(100, 100, 6, 1, 0, 0, 6, 50);
	}
	else {
		sprite.setAnim(100, 100, 6, 1, 200, 0, 6, 50);
	}
	state = RIGHT;
	setSize(sprite);
}

//Sets the state of the player to facing left
// Set the animation accordingly depending on if the player is invincible
// Set the velocity to move the player
void PlayerEmitter::setLeft() {
	rotateRight = false;
	velocity.x = -15;
	childVelocity.x = -2000;
	if (!invincible) {
		sprite.setAnim(100, 100, 6, 1, 100, 0, 6, 50);
	}
	else {
		sprite.setAnim(100, 100, 6, 1, 300, 0, 6, 50);
	}
	state = LEFT;
	setSize(sprite);
}

//Sets the state of the player to idle
// Set the animation accordingly depending on if the player is invincible and if the player is facing right or left
// Set the velocity to stop the player
void PlayerEmitter::setIdle() {
		if (rotateRight) {
			childVelocity.x = 2000;
			if (!invincible) {
				sprite.setAnim(100, 100, 6, 1, 0, 0, 6, 50);
			}
			else {
				sprite.setAnim(100, 100, 6, 1, 200, 0, 6, 50);
			}
		}
		else {
			childVelocity.x = -2000;
			if (!invincible) {
				sprite.setAnim(100, 100, 6, 1, 100, 0, 6, 50);
			}
			else {
				sprite.setAnim(100, 100, 6, 1, 300, 0, 6, 50);
			}
		}
	velocity.x = 0;
	state = IDLE;
	setSize(sprite);
}


// If the player is it then the player becomes invincible
// Set the animation accordingly
// Reset application of existing forces and add particles to the particle system to emit
void PlayerEmitter::playerHit() {
	invincible = true;
	if (rotateRight) {
		sprite.setAnim(100, 100, 6, 1, 200, 0, 6, 50);
	}
	else {
		sprite.setAnim(100, 100, 6, 1, 300, 0, 6, 50);
	}
	partsys->reset();
	for (int i = 0; i < groupSize; i++) {
		particles[0].lifespan = .5;
		particles[0].setPosition(trans);
		particles[0].birthtime = ofGetElapsedTimeMillis();
		partsys->add(particles[0]);
	}
}

// Update the player emitter
// If started do not do anything
void PlayerEmitter::update() {
	if (!started) return;
	// Test for invincibility
	// If invincibility time is up, set invincible to false
	// Set the animation accordingly
	if (invincible && ofGetElapsedTimeMillis() - lastHit > invincibilityTime) {
		invincible = false;
		if (rotateRight) {
			sprite.setAnim(100, 100, 6, 1, 0, 0, 6, 50);
		}
		else {
			sprite.setAnim(100, 100, 6, 1, 100, 0, 6, 50);
		}
	}
	// Apply gravity to the velocity of the player
	if ((trans.y +height / 2) < ofGetWindowHeight()) {
		velocity.y += gravity;
	}
	trans += velocity; // update the position
	// These blocks are used for bounds checking and resets jumps upon touching the lower bound
	if (trans.y + height / 2 >= ofGetWindowHeight()) {
		setPosition(ofVec3f(trans.x, ofGetWindowHeight() - height / 2, 0));
		jumping = false;
		doubleJumping = false;
	}
	else if (trans.y - height / 2 <= 0) {
		setPosition(ofVec3f(trans.x, height / 2, 0));
	}
	if (trans.x + width / 2 >= ofGetWindowWidth()) {
		setPosition(ofVec3f(ofGetWindowWidth() - width / 2, trans.y, 0));
	}
	else if (trans.x - width / 2 <= 0) {
		setPosition(ofVec3f(width / 2, trans.y, 0));
	}
	// These blocks are for spawning projectiles
	// Basic fire is the long ranged attack
	// Secondary fire is the short ranged attacked
	float time = ofGetElapsedTimeMillis();
		if (basicFire) {
			if ((time - lastSpawned) > (1000.0 / childSpawnRate) && childSprites.size() != 0) {
				// spawn a new sprite
				childSprites[0].velocity = childVelocity;
				childSprites[0].lifespan = 2000;
				childSprites[0].birthtime = time;
				childSprites[0].trans = trans;
				sys->add(childSprites[0]);
				lastSpawned = time;
				if (childSprites[0].player.isLoaded()) {
					childSprites[0].player.play();
				}
			}
		}
		if (!invincible) { // Cannot short ranged attack while invincible
		if (secondaryFire) {
			if (!rotateRight) {
				int offset = (rand() % 3 * 125) + 21;
				childSprites[1].setAnim(125, 125, 1, 1, offset, 0, 1, 1000);
				childSprites[1].trans = trans - ofVec3f(100, 0, 0);
			}
			else {
				int offset = (rand() % 3 * 125) + 396;
				childSprites[1].setAnim(125, 125, 1, 1, offset, 0, 1, 1000);
				childSprites[1].trans = trans + ofVec3f(100, 0, 0);
			}
			if (childSprites[1].player.isLoaded()) {
				childSprites[1].player.play();
			}
			childSprites[1].lifespan = 100;
			childSprites[1].birthtime = time;
			sys->add(childSprites[1]);
			secondaryFire = false;
		}
	}
	sprite.setPosition(trans);
	sprite.update();
	if (partsys->particles.size() > 0) {
		partsys->update();
	}

	sys->update();
}  

// Resets values of the player
void PlayerEmitter::reset() {
	Emitter::stop();
	rotateRight = true;
	invincible = false;
	childVelocity.x = 2000;
	trans = ofVec3f(ofGetWindowWidth()/2, ofGetWindowHeight() - height/2, 0);
	setIdle();
}

// Renders the player and its sprites/particles
void PlayerEmitter::draw() {
	sprite.draw();
	if (partsys->particles.size() > 0) {
		partsys->draw();
	}
	sys->draw();
}

// Creates the first boss emitter
// Initilializes starting values
// Loads sprite from path passed into the constructor
// Boss 1 has an emitter system to control subemitters
Boss1Emitter::Boss1Emitter(SpriteSystem *spriteSys, ParticleSystem* particleSystem, EmitterSystem* emitterSystem, string spritePath) :Emitter(spriteSys, particleSystem) {
	width = 197;
	height = 250;
	maxLife = 200;
	life = maxLife;
	childSpawnRate = 1;
	childLifespan = 3000; 
	childVelocity = ofVec3f(-600, 0, 0); 
	generateSubEmitters("images/boss1_projectile_sprite_sheet.png");
	subEmitters[0].sprite.setAnim(100, 100, 2, 1, 0, 0, 2, 1000);
	generateSubEmitters("images/boss1_projectile_sprite_sheet.png");
	subEmitters[1].sprite.setAnim(100, 100, 2, 1, 100, 0, 2, 1000);
	if (image.loadImage(spritePath)) {
		sprite.setImage(image);
		setPhase1();
		haveImage = true;
	}
	else {
		haveImage = false;
		cout << "could not load sprite" << endl;
		ofExit();
	}
	subsys = emitterSystem;
	
}

// Generates sub emitters given an immage path
// Sub emitters are emitters so the boss emits subemitters which can further emit sprites or particles
// Initilializes starting values for sub emitters
// Adds it to the array of emitters so the boss can emit them
void Boss1Emitter::generateSubEmitters(string imagePath){
	Emitter* emitter = new Emitter(new SpriteSystem(), new ParticleSystem());
	emitter->generateSprite(imagePath);
	emitter->childVelocity = ofVec3f(0, -200, 0);
	emitter->childSpawnRate = 1;
	emitter->childLifespan = 1000;
	if (childImage.load(imagePath)) {
		emitter->setImage(childImage);
	}
	else {
		cout << "Cannot load sub emitter sprite" << endl;
		ofExit();
	}
	emitter->childSprites[0].setAnim(75, 75, 1, 1, 200, 0, 1, emitter->childLifespan);
	subEmitters.push_back(*emitter);
}

// Updates the boss emitter
// If started spawn sub emitters
// If not update the boss emitter to despawn particles/sprites/emitters
void Boss1Emitter::update() {
	if (started) {
		float time = ofGetElapsedTimeMillis();
		if ((time - lastSpawned) > (1000.0 / childSpawnRate) && subEmitters.size() != 0) {
			// spawn a new subemitter
			if (phase == PHASE1) {
				subEmitters[0].velocity = childVelocity;
				subEmitters[0].birthTime = time;
				subEmitters[0].trans = ofVec3f(trans.x, trans.y + height/4, 0);
				subEmitters[0].lifespan = childLifespan;
				subsys->add(subEmitters[0]);
				subEmitters[0].velocity = -childVelocity;
				subEmitters[0].birthTime = time;
				subEmitters[0].trans = ofVec3f(trans.x, trans.y + height / 4, 0);
				subEmitters[0].lifespan = childLifespan;
				subsys->add(subEmitters[0]);
			}
			if (phase == PHASE2) {
				subEmitters[1].start();
				subEmitters[1].velocity = childVelocity;
				subEmitters[1].birthTime = time;
				subEmitters[1].trans = ofVec3f(trans.x, trans.y + height / 4, 0);
				subEmitters[1].lifespan = childLifespan;
				subsys->add(subEmitters[1]);
				subEmitters[1].velocity = -childVelocity;
				subEmitters[1].birthTime = time;
				subEmitters[1].trans = ofVec3f(trans.x, trans.y + height / 4, 0);
				subEmitters[1].lifespan = childLifespan;
				subsys->add(subEmitters[1]);
			}
			lastSpawned = time;
		}
		trans += velocity / ofGetFrameRate();
	}
	sprite.setPosition(trans);
	sprite.update();
	subsys->update();
}

// Renders the boss emitter
void Boss1Emitter::draw() {
	sprite.draw();
	sys->draw();
	subsys->draw();
}

// Sets the boss to phase 1
// Sets the animation accordingly
void Boss1Emitter::setPhase1() {
	phase = PHASE1;
	sprite.setAnim(197, 250, 5, 1, 0, 53, 5, 100);
	subEmitters[0].sprite.startAnim();
}

// Sets the boss to phase 2
// Sets the animation accordingly
void Boss1Emitter::setPhase2() {
	phase = PHASE2;
	sprite.setAnim(197, 250, 5, 1, 250, 53, 5, 50);
	subEmitters[1].sprite.startAnim();
}

// Stops the boss emitter
// Stops the boss's sub emitters
// Sets life span of all sub emitters to 0 to delete them on the next update
void Boss1Emitter::stop() {
	Emitter::stop();
	for (int i = 0; i < subEmitters.size(); i++) {
		subEmitters[i].stop();
	}
	for (int i = 0; i < subsys->emitters.size(); i++) {
		subsys->emitters[i].lifespan = 0;
	}
}

// Resets the boss emitter's values and position
void Boss1Emitter::reset() {
	life = maxLife;
	setPhase1();
	trans = ofVec3f(ofGetWindowWidth() - width / 2, ofGetWindowHeight() - height / 2, 0);
	started = false;
}

//Creates the second boss emitter
//Initializes the starter values of the second boss emitter
// Generates all the sprites for the emitter
// In this case the velocities and lifespans are set ahead of time because they will never change
Boss2Emitter::Boss2Emitter(SpriteSystem *spriteSys, ParticleSystem* particleSystem, string spritePath) :Emitter(spriteSys, particleSystem) {
	width = 376;
	height = 376;
	maxLife = 300;
	life = maxLife;
	gravity = 10;
	jumping = false;
	swap = false;
	counter = 0;
	childSpawnRate = 1.25;
	childLifespan = 2000;
	childVelocity = ofVec3f(-600, 0, 0);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[0].setAnim(75, 75, 1, 1, 0, 0, 1, childLifespan);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[1].setAnim(75, 75, 1, 1, 75, 0, 1, childLifespan);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[2].setAnim(75, 75, 1, 1, 150, 0, 1, childLifespan);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[3].setAnim(75, 75, 1, 1, 225, 0, 1, childLifespan);

	childSprites[0].velocity = childVelocity;
	childSprites[0].lifespan = childLifespan;
	childSprites[1].velocity = -childVelocity;
	childSprites[1].lifespan = childLifespan;
	childSprites[2].velocity = ofVec3f(0, 600, 0);
	childSprites[2].lifespan = childLifespan;
	childSprites[3].velocity = ofVec3f(0, -600, 0);
	childSprites[3].lifespan = childLifespan;

	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[4].setAnim(91, 91, 1, 1, 300, 0, 1, childLifespan);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[5].setAnim(91, 91, 1, 1, 391, 0, 1, childLifespan);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[6].setAnim(91, 91, 1, 1, 482, 0, 1, childLifespan);
	generateSprite("images/boss2_projectile_sprite_sheet.png");
	childSprites[7].setAnim(91, 91, 1, 1, 573, 0, 1, childLifespan);


	childSprites[4].velocity = ofVec3f(-600, -600, 0);
	childSprites[4].lifespan = childLifespan;
	childSprites[5].velocity = ofVec3f(600, -600, 0);
	childSprites[5].lifespan = childLifespan;
	childSprites[6].velocity = ofVec3f(600, 600, 0);
	childSprites[6].lifespan = childLifespan;
	childSprites[7].velocity = ofVec3f(-600, 600, 0);
	childSprites[7].lifespan = childLifespan;

	if (image.loadImage(spritePath)) {
		sprite.setImage(image);
		setPhase1();
		haveImage = true;
	}
	else {
		haveImage = false;
		cout << "could not load sprite" << endl;
		ofExit();
	}

}

// If started emit sprites
// If not started continue updating to check for deletion of existing sprites
void Boss2Emitter::update() {
	if (started) {
		float time = ofGetElapsedTimeMillis();
		if ((time - lastSpawned) > (1000.0 / childSpawnRate)) {
			if (phase == PHASE1) {
				if (!swap) {
					childSprites[0].birthtime = time;
					childSprites[0].trans = trans;
					sys->add(childSprites[0]);
					childSprites[1].birthtime = time;
					childSprites[1].trans = trans;
					sys->add(childSprites[1]);
					childSprites[2].birthtime = time;
					childSprites[2].trans = trans;
					sys->add(childSprites[2]);
					childSprites[3].birthtime = time;
					childSprites[3].trans = trans;
					sys->add(childSprites[3]);
				} 
				else {
					childSprites[4].birthtime = time;
					childSprites[4].trans = trans;
					sys->add(childSprites[4]);
					childSprites[5].birthtime = time;
					childSprites[5].trans = trans;
					sys->add(childSprites[5]);
					childSprites[6].birthtime = time;
					childSprites[6].trans = trans;
					sys->add(childSprites[6]);
					childSprites[7].birthtime = time;
					childSprites[7].trans = trans;
					sys->add(childSprites[7]);
				}
				swap = !swap;
			}
			if (phase == PHASE2 || phase == PHASE3) {
				switch (counter % 4) {
				case 0: {
					childSprites[2].birthtime = time;
					childSprites[2].trans = trans;
					sys->add(childSprites[2]);
					childSprites[3].birthtime = time;
					childSprites[3].trans = trans;
					sys->add(childSprites[3]);
					break;
				}
				case 1: {
					childSprites[4].birthtime = time;
					childSprites[4].trans = trans;
					sys->add(childSprites[4]);
					childSprites[6].birthtime = time;
					childSprites[6].trans = trans;
					sys->add(childSprites[6]);
					break;
				}
				case 2: {
					childSprites[0].birthtime = time;
					childSprites[0].trans = trans;
					sys->add(childSprites[0]);
					childSprites[1].birthtime = time;
					childSprites[1].trans = trans;
					sys->add(childSprites[1]);
					break;
				}
				case 3: {
					childSprites[5].birthtime = time;
					childSprites[5].trans = trans;
					sys->add(childSprites[5]);
					childSprites[7].birthtime = time;
					childSprites[7].trans = trans;
					sys->add(childSprites[7]);
					break;
				}
				}
				counter++;
			}
			lastSpawned = time;
		}
		trans += velocity / ofGetFrameRate();
	}
	// Bounds checking for the sprites, sets lifespan to 0 if out of bounds
	for (int i = 0; i < sys->sprites.size(); i++) {
		if (sys->sprites[i].trans.x - sys->sprites[i].width / 2 < 0 || sys->sprites[i].trans.x + sys->sprites[i].width / 2 > ofGetWindowWidth() || sys->sprites[i].trans.y - sys->sprites[i].height / 2 < 0 || sys->sprites[i].trans.y + sys->sprites[i].height / 2 > ofGetWindowHeight()) {
			sys->sprites[i].lifespan = 0;
		}
	}
	sprite.setPosition(trans);
	sprite.update();
	sys->update();
}

// Renders the boss
void Boss2Emitter::draw() {
	sprite.draw();
	sys->draw();
}

// Sets the boss to phase 1
void Boss2Emitter::setPhase1() {
	childSpawnRate = 1.25;
	phase = PHASE1;
	sprite.setAnim(250, 250, 6, 1, 0, 0, 6, 50);
}

// Sets the boss to phase 2
void Boss2Emitter::setPhase2() {
	childSpawnRate = 4;
	phase = PHASE2;
	sprite.setAnim(250, 250, 6, 1, 250, 0, 6, 40);
}

// Sets the boss to phase 3
void Boss2Emitter::setPhase3() {
	phase = PHASE3;
	sprite.setAnim(250, 250, 6, 1, 500, 0, 6, 30);
}

// Applies gravity to the velocity of the boss
// Does bounds checking on the boss
// If the boss is not jumping then apply vertical velocity
void Boss2Emitter::checkJump() {
	if ((trans.y + height / 2) < ofGetWindowHeight()) {
		velocity.y += gravity;
	}
	if (trans.y + height / 2 >= ofGetWindowHeight()) {
		setPosition(ofVec3f(trans.x, ofGetWindowHeight() - height / 2, 0));
		jumping = false;
	}
	else if (trans.y - height / 2 <= 0) {
		setPosition(ofVec3f(trans.x, height / 2, 0));
	}
	if (trans.x + width / 2 >= ofGetWindowWidth()) {
		setPosition(ofVec3f(ofGetWindowWidth() - width / 2, trans.y, 0));
	}
	else if (trans.x - width / 2 <= 0) {
		setPosition(ofVec3f(width / 2, trans.y, 0));
	}
	if (!jumping) {
		velocity.y = -600;
		jumping = true;
	}
}

// Resets the values of the second boss
void Boss2Emitter::reset() {
	Emitter::stop();
	life = maxLife;
	setPhase1();
	trans = ofVec3f(ofGetWindowWidth() - width / 2, ofGetWindowHeight() - height / 2, 0);
	jumping = false;
	started = false;
	swap = false;
	childSpawnRate = 1.25;
	counter = 0;
}

// Creates the third boss emitter
// Initilizes starter values
// Create sprites for the emitter
// In this case it has a separate sprite lastProjectile for specific behavior on phase 12
Boss3Emitter::Boss3Emitter(SpriteSystem *spriteSys, ParticleSystem* particleSystem, string spritePath) :Emitter(spriteSys, particleSystem) {
	width = 300;
	height = 300;
	maxLife = 2400;
	life = maxLife;
	counter = 0;
	timeStopInterval = 6000;
	timeStopDuration = 2000;
	childSpawnRate = 1.25;
	childLifespan = 2000;
	childVelocity = ofVec3f(-600, 0, 0);
	generateSprite("images/boss3_projectile_sprite_sheet.png");
	childSprites[0].setAnim(80, 80, 1, 1, 0, 0, 1, childLifespan);
	generateAndAddParticle("images/boss3_projectile_sprite_sheet.png");
	particles[0].sprite.setAnim(80, 80, 1, 1, 160, 0, 1, childLifespan);

	if (image.loadImage(spritePath)) {
		sprite.setImage(image);
		setPhase1();
		haveImage = true;
	}
	else {
		haveImage = false;
		cout << "could not load sprite" << endl;
		ofExit();
	}
	if (image.loadImage("images/number12.png")) {
		lastProjectile.setImage(image);
	}
	else {
		haveImage = false;
		cout << "could not load sprite from: images/number12.png" << endl;
		ofExit();
	}
	lastProjectile.setPosition(ofVec3f(ofGetWindowWidth(),0 , 0));
}

// Updates the boss emitter
// If started spawn projectiles with behavior dependent on the phase
void Boss3Emitter::update() {
	if (started) {
		float time = ofGetElapsedTimeMillis();
		if ((time - lastSpawned) > (1000.0 / childSpawnRate)) {
			switch (phase) {
			case PHASE1:
			case PHASE2: {
				childSprites[0].velocity = childVelocity;
				childSprites[0].birthtime = time;
				childSprites[0].trans = trans;
				sys->add(childSprites[0]);
				break;
			}
			case PHASE3:
			case PHASE4: {
				particles[0].velocity = ofVec3f(ofRandom(-1,1), ofRandom(-1.25,0), 0);
				particles[0].velocity = particles[0].velocity * 1000;
				particles[0].lifespan = 4000 / 1000;
				particles[0].setPosition(trans);
				particles[0].birthtime = time;
				partsys->add(particles[0]);
				lastSpawned = time;
				break;
			}
			case PHASE5: 
			case PHASE6:{
				childSprites[0].velocity = ofVec3f(500,0,0);
				childSprites[0].birthtime = time;
				childSprites[0].trans = ofVec3f(trans.x, trans.y - height/2, 0);
				sys->add(childSprites[0]);
				childSprites[0].velocity = ofVec3f(-500,0,0);
				childSprites[0].birthtime = time;
				childSprites[0].trans = ofVec3f(trans.x, trans.y - height /2, 0);
				sys->add(childSprites[0]);
				childSprites[0].velocity = ofVec3f(1000, 0, 0);
				childSprites[0].birthtime = time;
				childSprites[0].trans = ofVec3f(trans.x, ofGetWindowHeight() - childSprites[0].height/2, 0);
				sys->add(childSprites[0]);
				childSprites[0].velocity = ofVec3f(-1000, 0, 0);
				childSprites[0].birthtime = time;
				childSprites[0].trans = ofVec3f(trans.x, ofGetWindowHeight() - childSprites[0].height / 2, 0);
				sys->add(childSprites[0]);
				break;
			}
			case PHASE7: {
				childSprites[0].velocity = childVelocity;
				childSprites[0].birthtime = time;
				childSprites[0].trans = trans;
				sys->add(childSprites[0]);
				break;
			}
			case PHASE8: {
				childSprites[0].velocity = ofVec3f(0, -600, 0);
				childSprites[0].birthtime = time;
				childSprites[0].trans = trans;
				sys->add(childSprites[0]);
				break;
			}
			}
			lastSpawned = time;
		}
		trans += velocity / ofGetFrameRate();
	}
	// Bounds checking for the sprites and particles
	for (int i = 0; i < sys->sprites.size(); i++) {
		if (sys->sprites[i].trans.x - sys->sprites[i].width / 2 < 0 || sys->sprites[i].trans.x + sys->sprites[i].width / 2 > ofGetWindowWidth() || sys->sprites[i].trans.y - sys->sprites[i].height / 2 < 0 || sys->sprites[i].trans.y + sys->sprites[i].height / 2 > ofGetWindowHeight()) {
			sys->sprites[i].lifespan = 0;
		}
	}
	for (int i = 0; i < partsys->particles.size(); i++) {
		if (partsys->particles[i].position.x - partsys->particles[i].sprite.width / 2 < 0 || partsys->particles[i].position.x + partsys->particles[i].sprite.width / 2 > ofGetWindowWidth() || partsys->particles[i].position.y - partsys->particles[i].sprite.height / 2 < 0 || partsys->particles[i].position.y + partsys->particles[i].sprite.height / 2 > ofGetWindowHeight()) {
			partsys->particles[i].lifespan = 0;
		}
	}
	sprite.setPosition(trans);
	sprite.update();
	sys->update();
	partsys->update();
}

// Renders the boss
void Boss3Emitter::draw() {
	sprite.draw();
	sys->draw();
	partsys->draw();
	if (phase == PHASE12) {
		lastProjectile.draw();
	}
}

// Sets phase to 1
void Boss3Emitter::setPhase1() {
	childSpawnRate = 1;
	phase = PHASE1;
	sprite.setAnim(300, 300, 12, 1, 0, 0, 12, 1000);
	childSprites[0].setAnim(80, 80, 1, 1, 0, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 2
void Boss3Emitter::setPhase2() {
	lastSwapped = ofGetElapsedTimeMillis();
	childSpawnRate = 2;
	phase = PHASE2;
	sprite.setAnim(300, 300, 12, 1, 300, 0, 12, 1000);
	childSprites[0].setAnim(80, 80, 1, 1, 80, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 3
void Boss3Emitter::setPhase3() {
	phase = PHASE3;
	childSpawnRate = 8;
	sprite.setAnim(300, 300, 12, 1, 600, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 160, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 4
void Boss3Emitter::setPhase4() {
	lastSwapped = ofGetElapsedTimeMillis();
	phase = PHASE4;
	childSpawnRate = 8;
	sprite.setAnim(300, 300, 12, 1, 900, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 240, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 5
void Boss3Emitter::setPhase5() {
	phase = PHASE5;
	childSpawnRate = 2;
	sprite.setAnim(300, 300, 12, 1, 1200, 0, 12, 1000);
	childSprites[0].setAnim(80, 80, 1, 1, 320, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 6
void Boss3Emitter::setPhase6() {
	lastSwapped = ofGetElapsedTimeMillis();
	phase = PHASE6;
	childSpawnRate = 2;
	sprite.setAnim(300, 300, 12, 1, 1500, 0, 12, 1000);
	childSprites[0].setAnim(80, 80, 1, 1, 400, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 7
void Boss3Emitter::setPhase7() {
	phase = PHASE7;
	childSpawnRate = 1;
	sprite.setAnim(300, 300, 12, 1, 1800, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 480, 0, 1, childLifespan);
	childSprites[0].setAnim(80, 80, 1, 1, 480, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 8
void Boss3Emitter::setPhase8() {
	lastSwapped = ofGetElapsedTimeMillis();
	phase = PHASE8;
	childSpawnRate = 1;
	sprite.setAnim(300, 300, 12, 1, 2100, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 560, 0, 1, childLifespan);
	childSprites[0].setAnim(80, 80, 1, 1, 560, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 9
void Boss3Emitter::setPhase9() {
	phase = PHASE9;
	childSpawnRate = 0;
	sprite.setAnim(300, 300, 12, 1, 2400, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 640, 0, 1, childLifespan);
	childSprites[0].setAnim(80, 80, 1, 1, 640, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 10
void Boss3Emitter::setPhase10() {
	lastSwapped = ofGetElapsedTimeMillis();
	phase = PHASE10;
	childSpawnRate = 0;
	sprite.setAnim(300, 300, 12, 1, 2700, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 720, 0, 1, childLifespan);
	childSprites[0].setAnim(80, 80, 1, 1, 720, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 11
void Boss3Emitter::setPhase11() {
	phase = PHASE11;
	childSpawnRate = 1;
	sprite.setAnim(300, 300, 12, 1, 3000, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 800, 0, 1, childLifespan);
	childSprites[0].setAnim(80, 80, 1, 1, 800, 0, 1, childLifespan);
	sprite.startAnim();
}

// Sets phase to 12
void Boss3Emitter::setPhase12() {
	phase = PHASE12;
	childSpawnRate = 0;
	sprite.setAnim(300, 300, 12, 1, 3300, 0, 12, 1000);
	particles[0].sprite.setAnim(80, 80, 1, 1, 800, 0, 1, childLifespan);
	childSprites[0].setAnim(80, 80, 1, 1, 800, 0, 1, childLifespan);
	sprite.startAnim();
}

// Updates the last projectile
// The projectile starts above the window and has velocity applied in the y direction so it will fall to the bottom bound
// Once it reaches the bottom bound it moves in the -X direction as negative X velocity is applied
// The player is supposed to push this sprite back by decreasing its -X velocity
// If the last projectile has a positive velocity greater than 1 the increments of -X velocity is increased
// Sets the life of the boss proportional to the location of the last projectile
void Boss3Emitter::updateLastProjectile() {
	lastProjectile.velocity.y += .2;
	lastProjectile.trans.y += lastProjectile.velocity.y;
	if (lastProjectile.trans.y + lastProjectile.height / 2 > ofGetWindowHeight()) {
		lastProjectile.trans = ofVec3f(lastProjectile.trans.x, ofGetWindowHeight() - lastProjectile.height / 2, 0);
		if (lastProjectile.velocity.x <= 1) {
			lastProjectile.velocity.x -= .2;
		}
		else {
			lastProjectile.velocity.x -= .5;
		}
		life = maxLife / 12 - (maxLife / 12 * (lastProjectile.trans.x - lastProjectile.width / 2) / ofGetWindowWidth());
	}
	lastProjectile.trans.x += lastProjectile.velocity.x;
}

// Starts the boss emitter
void Boss3Emitter::start() {
	Emitter::start();
	lastSwapped = ofGetElapsedTimeMillis();
}

// Stops the boss emitter
void Boss3Emitter::stop() {
	lastSwapped = ofGetElapsedTimeMillis();
	started = false;
	sprite.stopAnim();
	for (int i = 0; i < sys->sprites.size(); i++) {
		sys->sprites[i].lifespan = -1;
		sys->sprites[i].velocity = ofVec3f(0, 0, 0);
	}
	for (int i = 0; i < partsys->particles.size(); i++) {
		partsys->particles[i].lifespan = -1;
		partsys->particles[i].velocity = ofVec3f(0, 0, 0);
	}
}

// Resets the values of the boss emitter
void Boss3Emitter::reset() {
	Emitter::stop();
	life = maxLife;
	lastProjectile.setPosition(ofVec3f(ofGetWindowWidth(), 0, 0));
	lastProjectile.velocity = ofVec3f(0, 0, 0);
	setPhase1();
	started = false;
	childSpawnRate = 1;
	counter = 0;
}