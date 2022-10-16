# ModernProtobuf

A modern implementation of protoc to generate java sources from protobuf schemas

### What is ModernProtobuf

Protoc, the default compiler for protobuf schemas, can generate classes for Java starting from a schema.
The generated code, though, is really verbose and not up to date with modern versions of Java.
Moreover, it is not really intended to be edited, which may be necessary if you want your code to be available to other
developers.
As a matter of fact most projects that work with Google's protobuf create wrappers around the already gigantic classes generated by Google to make them more usable.
Keeping in mind the previous points, I decided to start this project.
The minimum required version is Java 17, the latest LTS, and the generated code relies only on this library and lombok.
Performance wise, ModernProtobuf matches Jackson's Protobuf, but they both fall short when compared to Google's implementation which is more than two times faster.
Both protobuf 2 and 3 are supported.

### Schema generation

The schema generator CLI tool is developed inside the tool module.
It can be easily downloaded from the release tab or by compiling it manually using maven.

To get started, run the executable from any terminal passing generate as an argument:

```
Missing required parameter: '<protobuf>'
Usage: <main class> generate [-hV] [-o=<output>] [-p=<pack>] <protobuf>
Generates the java classes for a protobuf file
      <protobuf>          The protobuf file used to generate the java classes
  -h, --help              Show this help message and exit.
  -o, --output=<output>   The directory where the generated classes should be
                            outputted, by default a directory named schemas
                            will be created in the home directory
  -p, --package=<pack>    The package of the generated classes, by default none
                            is specified
  -V, --version           Print version information and exit.
```

Follow the instructions to generate the files you need. For example:

```
protoc generate ./protobufs/auth.proto --package it.auties.example --output ./src/main/java/it/auties/example
```

You can freely edit the generated schemas. The type of each protobuf field is inferred by using reflection when decoding
or the json description
when encoding. If you want to override the type infer system, use the `@ProtobufType` annotation and pass the desired
type as a parameter.
Obviously, the type contained in an encoded protobuf should be applicable to the new field type.
If you want to map a particular class to another type when used as a property in a protobuf schema, apply the same
procedure but to said class declaration.
This might be useful if for example you want to create a wrapper around a common property type around your schemas, but
cannot modify the behaviour of the server
sending the encoded protobuf.

### Serialization

First create a new protobuf object mapper:
```java
var mapper = new ProtobufMapper();
```

Any Protobuf object can be serialized to an array of bytes using this piece of this code:

```java
var result = mapper.encode(protobuf);
```

Similarly, an array of bytes can be converted to any protobuf object using this piece of this code:

```java
var result = mapper.readMessage(bytes, ProtobufMessage.class);
```

### Example Schema

Protobuf(12 LOC):

```protobuf
message AdReplyInfo {
    optional string advertiserName = 1;
    enum AdReplyInfoMediaType {
        NONE = 0;
        IMAGE = 1;
        VIDEO = 2;
    }
    optional AdReplyInfoMediaType mediaType = 2;
    optional bytes jpegThumbnail = 16;
    optional string caption = 17;
}
```

Modern Protoc(36 LOC):

```
@Jacksonized
@Builder
@Data
@Accessors(fluent = true)
public class AdReplyInfo {
  @ProtobufProperty(index = 1, type = STRING)
  private String advertiserName;

  @ProtobufProperty(index = 2, type = MESSAGE)
  private AdReplyInfoMediaType mediaType;

  @ProtobufProperty(index = 16, type = BYTES)
  private byte[] jpegThumbnail;

  @ProtobufProperty(index = 17, type = STRING)
  private String caption;

  @AllArgsConstructor
  @Accessors(fluent = true)
  public enum AdReplyInfoMediaType {
    NONE(0),
    IMAGE(1),
    VIDEO(2);

    @Getter
    private final int index;

    @JsonCreator
    public static AdReplyInfoMediaType forIndex(int index) {
      return Arrays.stream(values())
          .filter(entry -> entry.index() == index)
          .findFirst()
          .orElse(null);
    }
  }
}
```

Google's Protoc(268 LOC):

```java
public interface AdReplyInfoOrBuilder extends
      // @@protoc_insertion_point(interface_extends:it.auties.whatsapp4j.model.AdReplyInfo)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>optional string advertiserName = 1;</code>
     * @return Whether the advertiserName field is set.
     */
    boolean hasAdvertiserName();
    /**
     * <code>optional string advertiserName = 1;</code>
     * @return The advertiserName.
     */
    java.lang.String getAdvertiserName();
    /**
     * <code>optional string advertiserName = 1;</code>
     * @return The bytes for advertiserName.
     */
    com.google.protobuf.ByteString
        getAdvertiserNameBytes();

    /**
     * <code>optional .it.auties.whatsapp4j.model.AdReplyInfo.AdReplyInfoMediaType mediaType = 2;</code>
     * @return Whether the mediaType field is set.
     */
    boolean hasMediaType();
    /**
     * <code>optional .it.auties.whatsapp4j.model.AdReplyInfo.AdReplyInfoMediaType mediaType = 2;</code>
     * @return The mediaType.
     */
    it.auties.whatsapp4j.model.WhatsappProtobuf.AdReplyInfo.AdReplyInfoMediaType getMediaType();

    /**
     * <code>optional bytes jpegThumbnail = 16;</code>
     * @return Whether the jpegThumbnail field is set.
     */
    boolean hasJpegThumbnail();
    /**
     * <code>optional bytes jpegThumbnail = 16;</code>
     * @return The jpegThumbnail.
     */
    com.google.protobuf.ByteString getJpegThumbnail();

    /**
     * <code>optional string caption = 17;</code>
     * @return Whether the caption field is set.
     */
    boolean hasCaption();
    /**
     * <code>optional string caption = 17;</code>
     * @return The caption.
     */
    java.lang.String getCaption();
    /**
     * <code>optional string caption = 17;</code>
     * @return The bytes for caption.
     */
    com.google.protobuf.ByteString
        getCaptionBytes();
  }
  /**
   * Protobuf type {@code it.auties.whatsapp4j.model.AdReplyInfo}
   */
  public  static final class AdReplyInfo extends
      com.google.protobuf.GeneratedMessageLite<
          AdReplyInfo, AdReplyInfo.Builder> implements
      // @@protoc_insertion_point(message_implements:it.auties.whatsapp4j.model.AdReplyInfo)
      AdReplyInfoOrBuilder {
    private AdReplyInfo() {
      advertiserName_ = "";
      jpegThumbnail_ = com.google.protobuf.ByteString.EMPTY;
      caption_ = "";
    }
    /**
     * Protobuf enum {@code it.auties.whatsapp4j.model.AdReplyInfo.AdReplyInfoMediaType}
     */
    public enum AdReplyInfoMediaType
        implements com.google.protobuf.Internal.EnumLite {
      /**
       * <code>NONE = 0;</code>
       */
      NONE(0),
      /**
       * <code>IMAGE = 1;</code>
       */
      IMAGE(1),
      /**
       * <code>VIDEO = 2;</code>
       */
      VIDEO(2),
      ;

      /**
       * <code>NONE = 0;</code>
       */
      public static final int NONE_VALUE = 0;
      /**
       * <code>IMAGE = 1;</code>
       */
      public static final int IMAGE_VALUE = 1;
      /**
       * <code>VIDEO = 2;</code>
       */
      public static final int VIDEO_VALUE = 2;


      @java.lang.Override
      public final int getNumber() {
        return value;
      }

      /**
       * @param value The number of the enum to look for.
       * @return The enum associated with the given number.
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static AdReplyInfoMediaType valueOf(int value) {
        return forNumber(value);
      }

      public static AdReplyInfoMediaType forNumber(int value) {
        switch (value) {
          case 0: return NONE;
          case 1: return IMAGE;
          case 2: return VIDEO;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<AdReplyInfoMediaType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          AdReplyInfoMediaType> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<AdReplyInfoMediaType>() {
              @java.lang.Override
              public AdReplyInfoMediaType findValueByNumber(int number) {
                return AdReplyInfoMediaType.forNumber(number);
              }
            };

      public static com.google.protobuf.Internal.EnumVerifier 
          internalGetVerifier() {
        return AdReplyInfoMediaTypeVerifier.INSTANCE;
      }

      private static final class AdReplyInfoMediaTypeVerifier implements 
           com.google.protobuf.Internal.EnumVerifier { 
              static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new AdReplyInfoMediaTypeVerifier();
              @java.lang.Override
              public boolean isInRange(int number) {
                return AdReplyInfoMediaType.forNumber(number) != null;
              }
            };

      private final int value;

      private AdReplyInfoMediaType(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:it.auties.whatsapp4j.model.AdReplyInfo.AdReplyInfoMediaType)
    }

 public  static final class AdReplyInfo extends
      com.google.protobuf.GeneratedMessageLite<
          AdReplyInfo, AdReplyInfo.Builder> implements
      // @@protoc_insertion_point(message_implements:it.auties.whatsapp4j.model.AdReplyInfo)
      AdReplyInfoOrBuilder {
    private AdReplyInfo() {
      advertiserName_ = "";
      jpegThumbnail_ = com.google.protobuf.ByteString.EMPTY;
      caption_ = "";
    }
    /**
     * Protobuf enum {@code it.auties.whatsapp4j.model.AdReplyInfo.AdReplyInfoMediaType}
     */
    public enum AdReplyInfoMediaType
        implements com.google.protobuf.Internal.EnumLite {
      /**
       * <code>NONE = 0;</code>
       */
      NONE(0),
      /**
       * <code>IMAGE = 1;</code>
       */
      IMAGE(1),
      /**
       * <code>VIDEO = 2;</code>
       */
      VIDEO(2),
      ;

      /**
       * <code>NONE = 0;</code>
       */
      public static final int NONE_VALUE = 0;
      /**
       * <code>IMAGE = 1;</code>
       */
      public static final int IMAGE_VALUE = 1;
      /**
       * <code>VIDEO = 2;</code>
       */
      public static final int VIDEO_VALUE = 2;


      @java.lang.Override
      public final int getNumber() {
        return value;
      }

      /**
       * @param value The number of the enum to look for.
       * @return The enum associated with the given number.
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static AdReplyInfoMediaType valueOf(int value) {
        return forNumber(value);
      }

      public static AdReplyInfoMediaType forNumber(int value) {
        switch (value) {
          case 0: return NONE;
          case 1: return IMAGE;
          case 2: return VIDEO;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<AdReplyInfoMediaType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          AdReplyInfoMediaType> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<AdReplyInfoMediaType>() {
              @java.lang.Override
              public AdReplyInfoMediaType findValueByNumber(int number) {
                return AdReplyInfoMediaType.forNumber(number);
              }
            };

      public static com.google.protobuf.Internal.EnumVerifier 
          internalGetVerifier() {
        return AdReplyInfoMediaTypeVerifier.INSTANCE;
      }

      private static final class AdReplyInfoMediaTypeVerifier implements 
           com.google.protobuf.Internal.EnumVerifier { 
              static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new AdReplyInfoMediaTypeVerifier();
              @java.lang.Override
              public boolean isInRange(int number) {
                return AdReplyInfoMediaType.forNumber(number) != null;
              }
            };

      private final int value;

      private AdReplyInfoMediaType(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:it.auties.whatsapp4j.model.AdReplyInfo.AdReplyInfoMediaType)
    }
```
