# *SentiStorm* - Real-time Twitter Sentiment Classification based on Apache Storm

*SentiStorm* is based on [Apache Storm](https://storm.apache.org) and uses different machine learning techniques to identify the sentiment of a tweet. For example, *SentiStorm* uses **Part-of-Speech (POS) tags**, **Term Frequency-Inverse Document Frequency (TF-IDF)** and multiple **sentiment lexica** to extract a feature vector out of a tweet. This extracted feature vector is processed by a **Support Vector Machine (SVM)**, which predicts the sentiment based on a training dataset.

## Topology of *SentiStorm*

<table>
  <tr><td><img src="/docs/images/topology_parallelism.png" alt="Topology of SentiStorm" width="400"></td></tr>
  <tr><td align="center">Topology of <i>SentiStorm</i></td></tr>
</table>


### Tokenizer

### Preprocessor

### POS Tagger

### Feature Vector Generation

### Support Vector Machine (SVM)


## Quality of *SentiStorm*


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
  <tr><td colspan="2" align="center">Throughput of <i>SentiStorm</i> based on the SemEval 2013<br> dataset and <i>c3.8xlarge</i> EC2 nodes</td></tr>
</table>

## References


