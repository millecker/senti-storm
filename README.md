# *SentiStorm* - Real-time Twitter Sentiment Classification based on Apache Storm

*SentiStorm* is based on [Apache Storm](https://storm.apache.org) and uses different machine learning techniques to identify the sentiment of a tweet. For example, *SentiStorm* uses **Part-of-Speech (POS) tags**, **Term Frequency-Inverse Document Frequency (TF-IDF)** and multiple **sentiment lexica** to extract a feature vector out of a tweet. This extracted feature vector is processed by a **Support Vector Machine (SVM)**, which predicts the sentiment based on a training dataset.

## Topology of *SentiStorm*

<table>
  <tr><td><img src="/docs/images/topology_parallelism.png" alt="Topology of SentiStorm" width="500"></td></tr>
  <tr><td align="center">Topology of <i>SentiStorm</i></td></tr>
</table>


### Tokenizer

<table>
  <tr><td><img src="/docs/images/tokenizer.png" alt="Tokenizer workflow" width="250"></td></tr>
  <tr><td align="center">Tokenizer workflow</td></tr>
</table>

### Preprocessor

<table>
  <tr><td><img src="/docs/images/preprocessor.png" alt="Preprocessor workflow" width="250"></td></tr>
  <tr><td align="center">Preprocessor workflow</i></td></tr>
</table>

### POS Tagger

### Feature Vector Generation

### Support Vector Machine (SVM)


## Quality of *SentiStorm*

<table>
  <tr>
    <th>Features</th>
    <th colspan="8">SemEval 2013 Test</th>
  </tr>
  <tr>
    <td><br></td>
    <td><b>F_pos</b></td>
    <td><b>F_neg</b></td>
    <td><b>F_ntr</b></td>
    <td><b>F_all</b></td>
    <td><b>F_p/n</b></td>
    <td><b>Acc</b></td>
    <td><b>Difference <br>F_all</b></td>
    <td><b>Difference <br>F_p/n</b></td>
  </tr>
  <tr>
    <td><b>All Features</b></td>
    <td><b>.7080</b></td>
    <td><b>.6290</b></td>
    <td><b>.7251</b></td>
    <td><b>.7012</b></td>
    <td><b>.6685</b></td>
    <td><b>.7021</b></td>
    <td><br></td>
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
    <td>+.0011</td>
    <td>-.0354</td>
  </tr>
  <tr>
    <td>- TF-IDF</td>
    <td>.6380</td>
    <td>.7354</td>
    <td>.6689</td>
    <td>.6634</td>
    <td>.6398</td>
    <td>.6666</td>
    <td>-.0378</td>
    <td>-.0287</td>
  </tr>
  <tr>
    <td>- POS Tags</td>
    <td>.7049</td>
    <td>.6014</td>
    <td>.7148</td>
    <td>.6903</td>
    <td>.6531</td>
    <td>.6916</td>
    <td>-.0109</td>
    <td>-.0154</td>
  </tr>
  <tr>
    <td>- AFINN</td>
    <td>.6952</td>
    <td>.6138</td>
    <td>.7082</td>
    <td>.6857</td>
    <td>.6545</td>
    <td>.6869</td>
    <td>-.0155</td>
    <td>-.0140</td>
  </tr>
  <tr>
    <td>- SentiStrength</td>
    <td>.7070</td>
    <td>.6218</td>
    <td>.7247</td>
    <td>.6993</td>
    <td>.6644</td>
    <td>.7002</td>
    <td>-.0019</td>
    <td>-.0041</td>
  </tr>
  <tr>
    <td>- SentiStrength :-)</td>
    <td>.6938</td>
    <td>.6138</td>
    <td>.7180</td>
    <td>.6905</td>
    <td>.6538</td>
    <td>.6910</td>
    <td>-.0107</td>
    <td>-.0147</td>
  </tr>
  <tr>
    <td>- SentiWords</td>
    <td>.7003</td>
    <td>.6094</td>
    <td>.7246</td>
    <td>.6951</td>
    <td>.6549</td>
    <td>.6958</td>
    <td>-.0061</td>
    <td>-.0136</td>
  </tr>
  <tr>
    <td>- Sentiment140</td>
    <td>.6972</td>
    <td>.6051</td>
    <td>.7222</td>
    <td>.6918</td>
    <td>.6511</td>
    <td>.6926</td>
    <td>-.0094</td>
    <td>-.0174</td>
  </tr>
  <tr>
    <td>- Bing Liu</td>
    <td>.7031</td>
    <td>.6261</td>
    <td>.7242</td>
    <td>.6989</td>
    <td>.6646</td>
    <td>.6994</td>
    <td>-.0023</td>
    <td>-.0039</td>
  </tr>
  <tr>
    <td>- MPQA</td>
    <td>.7075</td>
    <td>.6159</td>
    <td>.7279</td>
    <td>.7002</td>
    <td>.6617</td>
    <td>.7010</td>
    <td>-.0010</td>
    <td>-.0068</td>
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


