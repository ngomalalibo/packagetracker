package com.logistics.packagetracker.codec;

import com.logistics.packagetracker.enumeration.PackageStatus;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class PackageStatusCodec implements Codec<PackageStatus>
{
    @Override
    public PackageStatus decode(BsonReader bsonReader, DecoderContext decoderContext)
    {
        return PackageStatus.fromValue(bsonReader.readString());
    }
    
    @Override
    public void encode(BsonWriter bsonWriter, PackageStatus status, EncoderContext encoderContext)
    {
        bsonWriter.writeString(status.getValue());
    }
    
    @Override
    public Class<PackageStatus> getEncoderClass()
    {
        return PackageStatus.class;
    }
}
