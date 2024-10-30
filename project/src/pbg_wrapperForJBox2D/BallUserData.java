package pbg_wrapperForJBox2D;

public class BallUserData 
{
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public int ballID;
	public BallParticle ballParticle;
	
	public BallUserData(int ballID, BallParticle ballParticle)
	{
		this.ballID = ballID;
		this.ballParticle = ballParticle;
	}
}
