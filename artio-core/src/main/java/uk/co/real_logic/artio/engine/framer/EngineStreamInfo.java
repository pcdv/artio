package uk.co.real_logic.artio.engine.framer;

public final class EngineStreamInfo
{
    private final long inboundIndexSubscriptionRegistrationId;
    private final long outboundIndexSubscriptionRegistrationId;
    private final int inboundPublicationSessionId;
    private final long inboundPublicationPosition;
    private final int outboundPublicationSessionId;
    private final long outboundPublicationPosition;

    EngineStreamInfo(
        final long inboundIndexSubscriptionRegistrationId,
        final long outboundIndexSubscriptionRegistrationId,
        final int inboundPublicationSessionId,
        final long inboundPublicationPosition,
        final int outboundPublicationSessionId,
        final long outboundPublicationPosition)
    {
        this.inboundIndexSubscriptionRegistrationId = inboundIndexSubscriptionRegistrationId;
        this.outboundIndexSubscriptionRegistrationId = outboundIndexSubscriptionRegistrationId;
        this.inboundPublicationSessionId = inboundPublicationSessionId;
        this.inboundPublicationPosition = inboundPublicationPosition;
        this.outboundPublicationSessionId = outboundPublicationSessionId;
        this.outboundPublicationPosition = outboundPublicationPosition;
    }

    public long inboundIndexSubscriptionRegistrationId()
    {
        return inboundIndexSubscriptionRegistrationId;
    }

    public long outboundIndexSubscriptionRegistrationId()
    {
        return outboundIndexSubscriptionRegistrationId;
    }

    public int inboundPublicationSessionId()
    {
        return inboundPublicationSessionId;
    }

    public long inboundPublicationPosition()
    {
        return inboundPublicationPosition;
    }

    public int outboundPublicationSessionId()
    {
        return outboundPublicationSessionId;
    }

    public long outboundPublicationPosition()
    {
        return outboundPublicationPosition;
    }

    public String toString()
    {
        return "EngineStreamInfo{" +
            "inboundIndexSubscriptionRegistrationId=" + inboundIndexSubscriptionRegistrationId +
            ", outboundIndexSubscriptionRegistrationId=" + outboundIndexSubscriptionRegistrationId +
            ", inboundPublicationSessionId=" + inboundPublicationSessionId +
            ", inboundPublicationPosition=" + inboundPublicationPosition +
            ", outboundPublicationSessionId=" + outboundPublicationSessionId +
            ", outboundPublicationPosition=" + outboundPublicationPosition +
            '}';
    }
}
