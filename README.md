# *SentiStorm* - Real-time Twitter Sentiment Classification based on Apache Storm

*SentiStorm* is based on [Apache Storm](https://storm.apache.org) \[1\] and uses different machine learning techniques to identify the sentiment of a tweet. For example, *SentiStorm* uses **Part-of-Speech (POS) tags**, **Term Frequency-Inverse Document Frequency (TF-IDF)** and multiple **sentiment lexica** to extract a feature vector out of a tweet. This extracted feature vector is processed by a **Support Vector Machine (SVM)**, which predicts the sentiment based on a training dataset.
The full thesis can be found [here](/docs/masterthesis.pdf).

## Topology of *SentiStorm*

<table>
  <tr><td><img src="/docs/images/topology_parallelism.png" alt="Topology of SentiStorm" width="500"></td></tr>
  <tr><td align="center">Topology of <i>SentiStorm</i></td></tr>
</table>

The figure above illustrates the topology of *SentiStorm* including its components. The *Dataset Spout* emits tweets from a local dataset into the Storm pipeline. It can be easily replaced by another *Spout*. For example, a *Twitter Spout* can be used to emit tweets directly from the real-time Twitter stream. After tweets have been emitted by a *Spout*, the *Tokenizer* replaces possible Unicode or HTML symbols and tokenizes the tweet text by a complex regular expression. Then, each token is processed by the *Preprocessor*, which tries to unify emoticons, fix slang language or gerund forms and remove elongations. The unification of emotions removes repeating characters to get a consistent set of emoticons. For example, the emoticon :-))) is replaced by :-) and therefore the sentiment can be easily obtained from an emoticon lexicon. Slang expressions such as *omg* are substituted by *oh my god* by the usage of multiple slang lexica. Gerund forms are fixed by checking the ending of words for an omitted *g* such as in *goin*. The remove elongations process is equivalent to the unification of emoticons and tries to eliminate repeating characters such as in *suuuper*. After the *Preprocessor*, a *POS Tagger* predicts  the part-of-speech label for each token and forwards them to the *Feature Vector Generation*. The feature extraction process is a key component in *SentiStorm*. It generates a feature vector for each tweet based on the previously gathered data. The *Feature Vector Generation* component uses TF-IDF, POS tags and multiple sentiment lexica to map a tweet text into numerical features. Based on this feature vector the SVM component is finally able to predict the sentiment of the given tweet.


### Tokenizer

The *Tokenizer* is the first *Bolt* in the *SentiStorm* topology and splits a tweet text into several tokens. In this process, the *Tokenizer* uses pattern matching with regular expressions. Furthermore, it replaces Unicode or HTML symbols before tokenizing the tweet text.

<table>
  <tr><td><img src="/docs/images/tokenizer.png" alt="Tokenizer workflow" width="250"></td></tr>
  <tr><td align="center">Tokenizer workflow</td></tr>
</table>

### Preprocessor

The *Preprocessor* component receives the tokenized tweet from the *Tokenizer* and prepares the tokens for the *POS Tagger*. The following figure illustrates the workflow of the *Preprocessor*, which consists of multiple steps.

<table>
  <tr><td><img src="/docs/images/preprocessor.png" alt="Preprocessor workflow" width="250"></td></tr>
  <tr><td align="center">Preprocessor workflow</i></td></tr>
</table>

In the first step, the *Preprocessor* unifies all emoticons. For example, the emoticon :-))) will become :-) to get a consistent set of emoticons. *SentiStorm* does currently not differentiate between these two emoticons, both of them have the same positive sentiment score based on the SentiStrength \[1\] emoticons lexicon. Future extensions of *SentiStorm* might differentiate between these emoticons by using boost sentiment scores. In the second step, the *Preprocessor* tries to substitute slang expressions. The replacement of slang expressions will help the *POS Tagger* to determine the right POS tag. The next step fixes possible punctuations between characters. For example, the term *L.O.V.E* is replaced by the term *LOVE*. The *Preprocessor* also fixes incomplete gerund forms such as *goin* by replacing it with *going*. For that purpose, it uses the *WordNet* \[1\] dictionary to find a valid word. In the last step, elongations such as *suuuper* are removed. If an elongation has been removed by the *Preprocessor*, then it has to check the term for any slang expression again.

### POS Tagger

The *POS Tagger* component determines the part-of-speech (POS) labels for the preprocessed tokens. Currently there are two major POS taggers available, which are highly specialized for the Twitter-specific language. The first POS tagger was presented by Derczynski et al. \[1\] of the General Architecture for Text Engineering (GATE) group at the University of Sheffield. Owoputi et al. \[2\] of the ARK research group at the Carnegie Mellon University proposed the second major POS tagger.

The first implementation of *SentiStorm* used the GATE POS tagger because of the commonly used PTB tagset support. But the major drawback in speed of the GATE tagger made a transition to the ARK tagger necessary. The GATE tagger is significantly slower than the ARK tagger and therefore it is not applicable in a real-time environment such as Storm. A performance comparison between the GATE and ARK tagger can be found in the performance evaluation section.

### Feature Vector Generation

The feature extraction process is a key component of *SentiStorm*. It is responsible for the predicting quality of the follow-up *Support Vector Machine* component. The *Feature Vector Generation* component extracts numerical features out of the preprocessed and tagged tweets. For that purpose, it uses a rich feature set, which consists of Term Frequency-Inverse Document Frequency (TF-IDF), POS tags and sentiment lexica.

The following table presents the different sentiment lexica, which are used by *SentiStorm*. It also includes the number of terms and the range of the sentiment scores. Each sentiment lexicon consists of a set of tokens, which are assigned by a sentiment score.

| Sentiment Lexicon | # of Terms | Scores |
|-------------------------|:---------------:|:--------------------:|
| AFINN-111 \[1\] | 2477 words | [-5, 5] |
| SentiStrength Emotions \[1\] | 2,544 regex | [-5, 5] |
| SentiStrength Emoticons \[1\] | 107 emoticons | [-1, 1] |
| SentiWords \[1\] | 147,292 words | [-0.935, 0.88257] |
| Sentiment140 \[1\] | 62,468 unigrams | [-4.999, 5] |
| Bing Liu \[1\] | 6,785 words | [positive, negative] |
| MPQA Subjectivity \[1\] | 6,886 words | [positive, negative] |

### Support Vector Machine (SVM)

The last component of the *SentiStorm* topology is the *Support Vector Machine* (SVM). SVM is used to classify the sentiment of a tweet based on its feature vector. It is a supervised learning model and requires a set of training data and associated labels. The training data consist of feature vectors, which are usually defined by numerical values. The SVM tries to find hyperplanes that separate these training vectors based on their associated labels. Then all future feature vectors can be classified. *SentiStorm* uses the *LIBSVM* library of Chang et al. \[1\]. It is a well-known SVM implementation in the machine learning area.

## Quality of *SentiStorm*

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
    <td>- AFINN</td>
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
    <td>- SentiStrength</td>
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
    <td>- SentiStrength :-)</td>
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
    <td>- SentiWords</td>
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
    <td>- Sentiment140</td>
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
    <td>- Bing Liu</td>
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
    <td>- MPQA</td>
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


| Nodes | Tokenizer Latency (ms) | Preprocessor Latency (ms) | POS Tagger Latency (ms) | Feature Generation Latency (ms) | SVM Latency (ms) | Complete Latency (ms) |
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

## References

\[1\] https://storm.apache.org

