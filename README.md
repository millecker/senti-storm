# *SentiStorm* - Real-time Twitter Sentiment Classification based on Apache Storm

*SentiStorm* is based on [Apache Storm](https://storm.apache.org) and uses different machine learning techniques to identify the sentiment of a tweet. For example, *SentiStorm* uses **Part-of-Speech (POS) tags**, **Term Frequency-Inverse Document Frequency (TF-IDF)** and multiple **sentiment lexica** to extract a feature vector out of a tweet. This extracted feature vector is processed by a **Support Vector Machine (SVM)**, which predicts the sentiment based on a training dataset.

The full thesis can be found [here](/docs/masterthesis.pdf).

## Topology of *SentiStorm*

<table>
  <tr><td><img src="/docs/images/topology_parallelism.png" alt="Topology of SentiStorm" width="500"></td></tr>
  <tr><td align="center">Topology of <i>SentiStorm</i></td></tr>
</table>

The figure above illustrates the topology of *SentiStorm* including its components. The [*Dataset Spout*](/src/at/illecker/sentistorm/spout/DatasetSpout.java) emits tweets from a local dataset into the Storm pipeline. It can be easily replaced by another *Spout*. For example, a [*Twitter Spout*](/src/at/illecker/sentistorm/spout/TwitterStreamSpout.java) can be used to emit tweets directly from the real-time Twitter stream. After tweets have been emitted by a *Spout*, the [*Tokenizer*](/src/at/illecker/sentistorm/bolt/TokenizerBolt.java) replaces possible Unicode or HTML symbols and tokenizes the tweet text by a complex regular expression. Then, each token is processed by the [*Preprocessor*](/src/at/illecker/sentistorm/bolt/PreprocessorBolt.java), which tries to unify emoticons, fix slang language or gerund forms and remove elongations. The unification of emotions removes repeating characters to get a consistent set of emoticons. For example, the emoticon :-))) is replaced by :-) and therefore the sentiment can be easily obtained from an emoticon lexicon. Slang expressions such as *omg* are substituted by *oh my god* by the usage of multiple slang lexica. Gerund forms are fixed by checking the ending of words for an omitted *g* such as in *goin*. The remove elongations process is equivalent to the unification of emoticons and tries to eliminate repeating characters such as in *suuuper*. After the [*Preprocessor*](/src/at/illecker/sentistorm/bolt/PreprocessorBolt.java), a [*POS Tagger*](/src/at/illecker/sentistorm/bolt/POSTaggerBolt.java) predicts  the part-of-speech label for each token and forwards them to the [*Feature Vector Generation*](/src/at/illecker/sentistorm/bolt/FeatureGenerationBolt.java). The feature extraction process is a key component in *SentiStorm*. It generates a feature vector for each tweet based on the previously gathered data. The [*Feature Vector Generation*](/src/at/illecker/sentistorm/bolt/FeatureGenerationBolt.java) component uses TF-IDF, POS tags and multiple sentiment lexica to map a tweet text into numerical features. Based on this feature vector the [*SVM*](/src/at/illecker/sentistorm/bolt/SVMBolt.java) component is finally able to predict the sentiment of the given tweet.

The figure also illustrates the corresponding [parallelism hint](https://storm.apache.org/documentation/Understanding-the-parallelism-of-a-Storm-topology.html) of each component. The parallelism value depends on the number of workers or nodes *n*. For example, the parallelism value of the [*POS Tagger*](/src/at/illecker/sentistorm/bolt/POSTaggerBolt.java) component is 50 for a 10-node cluster, which means that each node executes 5 threads. These parallelism values fully utilize the 32 cores of a [*c3.8xlarge*](http://aws.amazon.com/ec2/instance-types/) instance, because the [*LIBSVM*](https://github.com/millecker/libsvm) library uses multiple threads too.

### Tokenizer

The [*Tokenizer*](/src/at/illecker/sentistorm/bolt/TokenizerBolt.java) is the first *Bolt* in the *SentiStorm* topology and splits a tweet text into several tokens. In this process, the [*Tokenizer*](/src/at/illecker/sentistorm/components/Tokenizer.java) uses pattern matching with regular expressions. Furthermore, it replaces Unicode or HTML symbols before tokenizing the tweet text.

<table>
  <tr><td><img src="/docs/images/tokenizer.png" alt="Tokenizer workflow" width="250"></td></tr>
  <tr><td align="center">Tokenizer workflow</td></tr>
</table>

### Preprocessor

The [*Preprocessor*](/src/at/illecker/sentistorm/bolt/PreprocessorBolt.java) component receives the tokenized tweet from the [*Tokenizer*](/src/at/illecker/sentistorm/bolt/TokenizerBolt.java) and prepares the tokens for the [*POS Tagger*](/src/at/illecker/sentistorm/bolt/POSTaggerBolt.java). The following figure illustrates the workflow of the [*Preprocessor*](/src/at/illecker/sentistorm/components/Preprocessor.java), which consists of multiple steps.

<table>
  <tr><td><img src="/docs/images/preprocessor.png" alt="Preprocessor workflow" width="250"></td></tr>
  <tr><td align="center">Preprocessor workflow</i></td></tr>
</table>

In the first step, the [*Preprocessor*](/src/at/illecker/sentistorm/components/Preprocessor.java) unifies all emoticons. For example, the emoticon :-))) will become :-) to get a consistent set of emoticons. *SentiStorm* does currently not differentiate between these two emoticons, both of them have the same positive sentiment score based on the [SentiStrength](http://sentistrength.wlv.ac.uk) emoticons lexicon. Future extensions of *SentiStorm* might differentiate between these emoticons by using boost sentiment scores. In the second step, the *Preprocessor* tries to substitute slang expressions. The replacement of slang expressions will help the *POS Tagger* to determine the right POS tag. The next step fixes possible punctuations between characters. For example, the term *L.O.V.E* is replaced by the term *LOVE*. The [*Preprocessor*](/src/at/illecker/sentistorm/components/Preprocessor.java) also fixes incomplete gerund forms such as *goin* by replacing it with *going*. For that purpose, it uses the [*WordNet*](https://wordnet.princeton.edu) dictionary to find a valid word. In the last step, elongations such as *suuuper* are removed. If an elongation has been removed by the [*Preprocessor*](/src/at/illecker/sentistorm/components/Preprocessor.java), then it has to check the term for any slang expression again.

### POS Tagger

The [*POS Tagger*](/src/at/illecker/sentistorm/bolt/POSTaggerBolt.java) component determines the part-of-speech (POS) labels for the preprocessed tokens. Currently there are two major POS taggers available, which are highly specialized for the Twitter-specific language. The first POS tagger was presented by Derczynski et al. \[1\] of the [General Architecture for Text Engineering (GATE)](https://gate.ac.uk) group at the University of Sheffield. Owoputi et al. \[2\] of the [ARK research group](http://www.ark.cs.cmu.edu) at the Carnegie Mellon University proposed the second major POS tagger.

The first implementation of *SentiStorm* used the [*GATE* POS tagger](https://gate.ac.uk/wiki/twitter-postagger.html) because of the commonly used [PTB tagset](http://www.comp.leeds.ac.uk/amalgam/tagsets/upenn.html) support. But the major drawback in speed of the [*GATE* POS tagger](https://gate.ac.uk/wiki/twitter-postagger.html) made a transition to the [*ARK* tagger](http://www.ark.cs.cmu.edu/TweetNLP/) necessary. The [*GATE* tagger](https://gate.ac.uk/wiki/twitter-postagger.html) is significantly slower than the [*ARK* tagger](http://www.ark.cs.cmu.edu/TweetNLP/) and therefore it is not applicable in a real-time environment such as Storm.

### Feature Vector Generation

The feature extraction process is a key component of *SentiStorm*. It is responsible for the predicting quality of the follow-up [*Support Vector Machine*](/src/at/illecker/sentistorm/bolt/SVMBolt.java) component. The [*Feature Vector Generation*](/src/at/illecker/sentistorm/bolt/FeatureGenerationBolt.java) component extracts numerical features out of the preprocessed and tagged tweets. For that purpose, it uses a rich feature set, which consists of Term Frequency-Inverse Document Frequency (TF-IDF), POS tags and sentiment lexica.

The following table presents the different sentiment lexica, which are used by *SentiStorm*. It also includes the number of terms and the range of the sentiment scores. Each sentiment lexicon consists of a set of tokens, which are assigned by a sentiment score.

| Sentiment Lexicon | # of Terms | Scores |
|-------------------------|:---------------:|:--------------------:|
| [AFINN-111](http://www2.imm.dtu.dk/pubdb/views/publication_details.php?id=6010) | 2477 words | [-5, 5] |
| [SentiStrength Emotions](http://sentistrength.wlv.ac.uk) | 2,544 regex | [-5, 5] |
| [SentiStrength Emoticons](http://sentistrength.wlv.ac.uk) | 107 emoticons | [-1, 1] |
| [SentiWords](https://hlt.fbk.eu/technologies/sentiwords) | 147,292 words | [-0.935, 0.88257] |
| [Sentiment140](http://www.saifmohammad.com/WebPages/lexicons.html) | 62,468 unigrams | [-4.999, 5] |
| [Bing Liu](http://www.cs.uic.edu/~liub/FBS/sentiment-analysis.html) | 6,785 words | [positive, negative] |
| [MPQA Subjectivity](http://mpqa.cs.pitt.edu/lexicons/subj_lexicon/) | 6,886 words | [positive, negative] |

### Support Vector Machine (SVM)

The last component of the *SentiStorm* topology is the [*Support Vector Machine*](/src/at/illecker/sentistorm/bolt/SVMBolt.java). SVM is used to classify the sentiment of a tweet based on its feature vector. It is a supervised learning model and requires a set of training data and associated labels. The training data consist of feature vectors, which are usually defined by numerical values. The SVM tries to find hyperplanes that separate these training vectors based on their associated labels. Then all future feature vectors can be classified. *SentiStorm* uses the [*LIBSVM*](http://www.csie.ntu.edu.tw/~cjlin/libsvm/) library of Chang et al. \[3\], which is a well-known SVM implementation in the machine learning area.

## Quality of *SentiStorm*

The quality evaluation compares the sentiment prediction quality of *SentiStorm* with state-of-art sentiment classification systems based on the [**SemEval 2013**](http://www.cs.york.ac.uk/semeval-2013/task2/) dataset. The F<sub>p/n</sub>-measure of *SentiStorm* is **66.85%**, which would achieve the second place in the top five SemEval message polarity results of 2013. The following table shows the top five [SemEval Message Polarity](http://www.cs.york.ac.uk/semeval-2013/accepted/101_Paper.pdf) \[4\] results of 2013.

| Team | F<sub>p/n</sub> |
|------------|--------|
| NRC-Canada | 0.6902 |
| GU-MLT-LT | 0.6527 |
| teragram | 0.6486 |
| BOUNCE | 0.6353 |
| KLUE | 0.6306 |

The feature ablation of the following table illustrates how much impact different features have on the overall prediction quality. Each row presents F-measures, which are obtained by subtracting one feature from all features. The most important features are the class weights and TF-IDF, which improve the F-measure by 0.0354 and 0.0287. The sentiment lexica of [Bing Liu](http://www.cs.uic.edu/~liub/FBS/sentiment-analysis.html) and [MPQA](http://mpqa.cs.pitt.edu/lexicons/subj_lexicon/) have only a minimal impact in the prediction quality.

<table>
  <tr>
    <th>Features</th>
    <th colspan="8">SemEval 2013 Test</th>
  </tr>
  <tr>
    <td><br></td>
    <td align="center"><b>F<sub>pos</sub></b></td>
    <td align="center"><b>F<sub>neg</sub></b></td>
    <td align="center"><b>F<sub>ntr</sub></b></td>
    <td align="center"><b>F<sub>all</sub></b></td>
    <td align="center"><b>F<sub>p/n</sub></b></td>
    <td align="center"><b>Acc</b></td>
    <td align="center"><b>D<sub>F<sub>all</sub></sub></b></td>
    <td align="center"><b>D<sub>F<sub>p/n</sub></sub></b></td>
  </tr>
  <tr>
    <td><b>All Features</b></td>
    <td><b>.7080</b></td>
    <td><b>.6290</b></td>
    <td><b>.7251</b></td>
    <td><b>.7012</b></td>
    <td><b>.6685</b></td>
    <td><b>.7021</b></td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>- Class Weights</td>
    <td>.7021</td>
    <td>.5642</td>
    <td>.7302</td>
    <td>.7023</td>
    <td>.6331</td>
    <td>.6974</td>
    <td align="right">+.0011</td>
    <td align="right">-.0354</td>
  </tr>
  <tr>
    <td>- TF-IDF</td>
    <td>.6380</td>
    <td>.7354</td>
    <td>.6689</td>
    <td>.6634</td>
    <td>.6398</td>
    <td>.6666</td>
    <td align="right">-.0378</td>
    <td align="right">-.0287</td>
  </tr>
  <tr>
    <td>- POS Tags</td>
    <td>.7049</td>
    <td>.6014</td>
    <td>.7148</td>
    <td>.6903</td>
    <td>.6531</td>
    <td>.6916</td>
    <td align="right">-.0109</td>
    <td align="right">-.0154</td>
  </tr>
  <tr>
    <td>- <a href="http://www2.imm.dtu.dk/pubdb/views/publication_details.php?id=6010">AFINN</a></td>
    <td>.6952</td>
    <td>.6138</td>
    <td>.7082</td>
    <td>.6857</td>
    <td>.6545</td>
    <td>.6869</td>
    <td align="right">-.0155</td>
    <td align="right">-.0140</td>
  </tr>
  <tr>
    <td>- <a href="http://sentistrength.wlv.ac.uk">SentiStrength</a></td>
    <td>.7070</td>
    <td>.6218</td>
    <td>.7247</td>
    <td>.6993</td>
    <td>.6644</td>
    <td>.7002</td>
    <td align="right">-.0019</td>
    <td align="right">-.0041</td>
  </tr>
  <tr>
    <td>- <a href="http://sentistrength.wlv.ac.uk">SentiStrength :-)</a></td>
    <td>.6938</td>
    <td>.6138</td>
    <td>.7180</td>
    <td>.6905</td>
    <td>.6538</td>
    <td>.6910</td>
    <td align="right">-.0107</td>
    <td align="right">-.0147</td>
  </tr>
  <tr>
    <td>- <a href="https://hlt.fbk.eu/technologies/sentiwords">SentiWords</a></td>
    <td>.7003</td>
    <td>.6094</td>
    <td>.7246</td>
    <td>.6951</td>
    <td>.6549</td>
    <td>.6958</td>
    <td align="right">-.0061</td>
    <td align="right">-.0136</td>
  </tr>
  <tr>
    <td>- <a href="http://www.saifmohammad.com/WebPages/lexicons.html">Sentiment140</a></td>
    <td>.6972</td>
    <td>.6051</td>
    <td>.7222</td>
    <td>.6918</td>
    <td>.6511</td>
    <td>.6926</td>
    <td align="right">-.0094</td>
    <td align="right">-.0174</td>
  </tr>
  <tr>
    <td>- <a href="http://www.cs.uic.edu/~liub/FBS/sentiment-analysis.html">Bing Liu</a></td>
    <td>.7031</td>
    <td>.6261</td>
    <td>.7242</td>
    <td>.6989</td>
    <td>.6646</td>
    <td>.6994</td>
    <td align="right">-.0023</td>
    <td align="right">-.0039</td>
  </tr>
  <tr>
    <td>- <a href="http://mpqa.cs.pitt.edu/lexicons/subj_lexicon/">MPQA</a></td>
    <td>.7075</td>
    <td>.6159</td>
    <td>.7279</td>
    <td>.7002</td>
    <td>.6617</td>
    <td>.7010</td>
    <td align="right">-.0010</td>
    <td align="right">-.0068</td>
  </tr>
</table>

## Performance of *SentiStorm*

The performance evaluation analyzes the speed of *SentiStorm*. The speed is mostly measured in tuples per second, which in this case are tweets per second. The performance evaluations are based on Amazon [*c3.8xlarge*](http://aws.amazon.com/ec2/instance-types/) EC2 instances. The Storm multi-node cluster consists of a single worker per node and goes up to 10 nodes.

The following table illustrates the latency of each *SentiStorm* component and the complete latency of the topology. The *Preprocessor* has the lowest latency of about 0.108 ms. The *POS Tagger* component has the highest latency. It needs about 1.53 ms to process one tweet, which is more than 10 times slower than the *Preprocessor*. *SVM* is slightly faster with a latency of 1.025 ms. The table also shows only a minimal increase in latency for multiple nodes. The complete latency of the topology is about 53.5 ms, which means that it takes 53.5 ms to process a tweet throughout the complete topology. The topology of *SentiStorm* was optimized for high throughput, accepting a higher latency. 

| Nodes | *Tokenizer* Latency (ms) | *Preprocessor Latency* (ms) | *POS Tagger* Latency (ms) | *Feature Generation Latency* (ms) | *SVM* Latency (ms) | Complete Latency (ms) |
|:-----:|:----------------------:|:-------------------------:|:-----------------------:|:-------------------------------:|:----------------:|:---------------------:|
| 1 | 0.179 | 0.108 | 1.492 | 0.185 | 0.953 | 48.155 |
| 2 | 0.182 | 0.108 | 1.514 | 0.183 | 0.987 | 51.048 |
| 3 | 0.189 | 0.112 | 1.531 | 0.183 | 1.034 | 52.607 |
| 4 | 0.187 | 0.109 | 1.543 | 0.180 | 1.023 | 52.311 |
| 5 | 0.188 | 0.110 | 1.536 | 0.183 | 1.023 | 52.657 |
| 6 | 0.184 | 0.108 | 1.532 | 0.179 | 1.025 | 53.332 |
| 7 | 0.182 | 0.109 | 1.544 | 0.178 | 1.022 | 53.575 |
| 8 | 0.187 | 0.110 | 1.549 | 0.178 | 1.025 | 53.359 |
| 9 | 0.180 | 0.107 | 1.521 | 0.177 | 1.016 | 54.055 |
| 10 | 0.182 | 0.107 | 1.528 | 0.176 | 1.031 | 53.889 |

The following table presents the throughput of *SentiStorm*. The throughput is measured in tweets per second at the end of the topology. The average number of tweets per second decreases only minimal from 1044 tweets per second at one node to 929 tweets per second at 10 nodes. This means that a single-node Storm cluster is able to execute **3133** tweets per second, which is only 20% less than the stand-alone performance. Based on Storm the *SentiStorm* topology scales almost linear and achieves **27,876** tweets per second at 10 nodes. These are **1,672,560** tweets per minute, **100,353,600** tweets per hour and **2,408,486,400** tweets per day. *SentiStorm* is able to predict the sentiment of each tweet of the global Twitter stream in real-time.

<table>
  <tr>
  <td>
  <table>
  <tr>
    <th>Nodes</th>
    <th>Tweets<br>per Second</th>
  </tr>
  <tr>
    <td align="center">1</td>
    <td align="center">3133</td>
  </tr>
  <tr>
    <td align="center">2</td>
    <td align="center">5920</td>
  </tr>
  <tr>
    <td align="center">3</td>
    <td align="center">8599</td>
  </tr>
  <tr>
    <td align="center">4</td>
    <td align="center">11528</td>
  </tr>
  <tr>
    <td align="center">5</td>
    <td align="center">14295</td>
  </tr>
  <tr>
    <td align="center">6</td>
    <td align="center">17025</td>
  </tr>
  <tr>
    <td align="center">7</td>
    <td align="center">19735</td>
  </tr>
  <tr>
    <td align="center">8</td>
    <td align="center">22576</td>
  </tr>
  <tr>
    <td align="center">9</td>
    <td align="center">25207</td>
  </tr>
  <tr>
    <td align="center">10</td>
    <td align="center">27876</td>
  </tr>
  </table>
  </td>
  <td><img src="/docs/images/performance_storm_c3.8xlarge.png" alt="SentiStorm Performance" width="500" /></td>
  </tr>
  <tr><td colspan="2" align="center">Throughput of <i>SentiStorm</i> based on the SemEval 2013 dataset and <i>c3.8xlarge</i> EC2 nodes</td></tr>
</table>

## Requirements

1. You have to download and place the [wn3.1.dict.tar.gz](http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz) file into [`resources/dictionaries/wordnet`](/resources/dictionaries/wordnet).
2. Modify supervisor childopts in `storm.yaml`

## Build and Run
You will need Java 7 and [Apache Ant](http://ant.apache.org) to build *SentiStorm*.

You can simply build with:
> `ant jar`

You can simply run *SentiStorm* with:
> `ant run`

## References

\[1\] https://gate.ac.uk/sale/ranlp2013/twitter_pos/twitter_pos.pdf

\[2\] http://www.ark.cs.cmu.edu/TweetNLP/owoputi+etal.naacl13.pdf

\[3\] http://www.csie.ntu.edu.tw/~cjlin/papers/libsvm.pdf

\[4\] http://www.cs.york.ac.uk/semeval-2013/accepted/101_Paper.pdf
