---


---

<h2 id="multilevel-parallel-patterns">Multilevel Parallel Patterns</h2>
<p>Mulitlevel Parallel Patterns (MPP for short) is a Java library for creating <em>structured</em> parallel applications that can be executed either on shared memory or distributed memory systems.</p>
<p>The intent to enable parallelism at two levels:</p>
<ul>
<li><strong>Distributed Memory level:</strong> Using the single program, multiple data programming model (SPMD), data can be scattered over a distributed system, with each node (a shared memory system) running an instance of the program. The scattered data is then processed in parallel by the nodes, and the output of each node is combined, giving us the final result of the parallel application. The distribution of data is achieved using message passing via the <em>MPJExpress</em> library. We have created a wrapper class (<code>MPPDistLib</code>) in order to provide intuitive access to common MPJExpress methods and fields.</li>
<li><strong>Shared Memory level:</strong> At the shared memory level, the MPP library aims to provide a <em>top-down</em> approach to structure parallel programs through the use of <em><strong>algorithmic skeletons</strong></em>, which are high-level programming models allowing for abstract descriptions of parallel programs. Each skeleton represents a generic computation pattern: users can simply express the desired parallel computation using these skeletons, the library will take care of the nitty-gritty execution details.<br>
Internally, the execution environment uses <em>Java threads</em> to execute computations as per the skeleton nesting.</li>
</ul>
<p>This library aims to greatly reduce the complexity involved in creating complex parallel applications by allowing users to focus on the <em>structure</em> of the parallel application rather than low-level implementation details.</p>
<p>This is <em><strong>a work in progress</strong></em>, refinements and additions are being made to make the library easier to use.</p>
<h2 id="prerequisites">Prerequisites</h2>
<p><strong>JDK 1.8</strong> or above and the latest version of <strong>maven</strong> are required to use the shared-memory level skeleton library to build parallel applications.</p>
<p>For running applications at the distributed level, the latest version of <strong>MPJExpress</strong> library needs to be installed in all participating nodes. The library and its documentation, including installation instructions, can be found here: <a href="http://mpj-express.org/">http://mpj-express.org/</a></p>
<p><strong>Note:</strong> The <em>MPJExpress</em> library can also be installed on a singular, multi-core, system and be made to run in a <em>Multicore</em> mode, which is useful for building and testing distributed applications when access to a distributed system is limited or unavailable; the application can then be deployed over a distributed system, once available, with minimal effort.</p>
<h2 id="usage">Usage</h2>
<p>The following workflow is typically used to create parallel applications using algorithmic skeletons:</p>
<ul>
<li>Express the parallel structure of the application by using proper skeleton nesting.</li>
<li>Provide application specific sequential portions of the code as skeleton parameters.</li>
<li>Compile/Link the resulting code to obtain the running parallel program</li>
</ul>
<p>For example, in the sample edge detection program provided with the library, there are three sequential operations that are performed in succession on an image. This can be represented by a pipeline skeleton structure:</p>
<div class="mermaid"><svg xmlns="http://www.w3.org/2000/svg" id="mermaid-svg-p9SsyZ198ix2k2Tv" width="100%" style="max-width: 234.76666259765625px;" viewBox="0 0 234.76666259765625 648.4332885742188"><g transform="translate(-12, -12)"><g class="output"><g class="clusters"></g><g class="edgePaths"><g class="edgePath" style="opacity: 1;"><path class="path" d="M129.38333129882812,88.81666564941406L129.38333129882812,127.17499542236328L129.38333129882812,165.5333251953125" marker-end="url(#arrowhead5987)" style="fill:none"></path><defs><marker id="arrowhead5987" viewBox="0 0 10 10" refX="9" refY="5" markerUnits="strokeWidth" markerWidth="8" markerHeight="6" orient="auto"><path d="M 0 0 L 10 5 L 0 10 z" class="arrowheadPath" style="stroke-width: 1px; stroke-dasharray: 1px, 0px;"></path></marker></defs></g><g class="edgePath" style="opacity: 1;"><path class="path" d="M129.38333129882812,212.24998474121094L129.38333129882812,250.60831451416016L129.38333129882812,288.9666442871094" marker-end="url(#arrowhead5988)" style="fill:none"></path><defs><marker id="arrowhead5988" viewBox="0 0 10 10" refX="9" refY="5" markerUnits="strokeWidth" markerWidth="8" markerHeight="6" orient="auto"><path d="M 0 0 L 10 5 L 0 10 z" class="arrowheadPath" style="stroke-width: 1px; stroke-dasharray: 1px, 0px;"></path></marker></defs></g><g class="edgePath" style="opacity: 1;"><path class="path" d="M129.38333129882812,335.6833038330078L129.38333129882812,374.04163360595703L129.38333129882812,412.39996337890625" marker-end="url(#arrowhead5989)" style="fill:none"></path><defs><marker id="arrowhead5989" viewBox="0 0 10 10" refX="9" refY="5" markerUnits="strokeWidth" markerWidth="8" markerHeight="6" orient="auto"><path d="M 0 0 L 10 5 L 0 10 z" class="arrowheadPath" style="stroke-width: 1px; stroke-dasharray: 1px, 0px;"></path></marker></defs></g><g class="edgePath" style="opacity: 1;"><path class="path" d="M129.38333129882812,459.1166229248047L129.38333129882812,497.4749526977539L129.38333129882812,535.8332824707031" marker-end="url(#arrowhead5990)" style="fill:none"></path><defs><marker id="arrowhead5990" viewBox="0 0 10 10" refX="9" refY="5" markerUnits="strokeWidth" markerWidth="8" markerHeight="6" orient="auto"><path d="M 0 0 L 10 5 L 0 10 z" class="arrowheadPath" style="stroke-width: 1px; stroke-dasharray: 1px, 0px;"></path></marker></defs></g></g><g class="edgeLabels"><g class="edgeLabel" style="opacity: 1;" transform="translate(129.38333129882812,127.17499542236328)"><g transform="translate(-39.92500305175781,-13.358329772949219)" class="label"><foreignObject width="79.85000610351562" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;"><span class="edgeLabel">Read image</span></div></foreignObject></g></g><g class="edgeLabel" style="opacity: 1;" transform="translate(129.38333129882812,250.60831451416016)"><g transform="translate(-33.349998474121094,-13.358329772949219)" class="label"><foreignObject width="66.69999694824219" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;"><span class="edgeLabel">3d matrix</span></div></foreignObject></g></g><g class="edgeLabel" style="opacity: 1;" transform="translate(129.38333129882812,374.04163360595703)"><g transform="translate(-62.099998474121094,-13.358329772949219)" class="label"><foreignObject width="124.19999694824219" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;"><span class="edgeLabel">Convolved matrix</span></div></foreignObject></g></g><g class="edgeLabel" style="opacity: 1;" transform="translate(129.38333129882812,497.4749526977539)"><g transform="translate(-64.95833587646484,-13.358329772949219)" class="label"><foreignObject width="129.9166717529297" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;"><span class="edgeLabel">Write result image</span></div></foreignObject></g></g></g><g class="nodes"><g class="node" style="opacity: 1;" id="X" transform="translate(129.38333129882812,54.40833282470703)"><circle x="-34.40833282470703" y="-23.35832977294922" r="34.40833282470703"></circle><g class="label" transform="translate(0,0)"><g transform="translate(-24.40833282470703,-13.358329772949219)"><foreignObject width="48.81666564941406" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;">Images</div></foreignObject></g></g></g><g class="node" style="opacity: 1;" id="A" transform="translate(129.38333129882812,188.89165496826172)"><rect rx="0" ry="0" x="-95.61666870117188" y="-23.35832977294922" width="191.23333740234375" height="46.71665954589844"></rect><g class="label" transform="translate(0,0)"><g transform="translate(-85.61666870117188,-13.358329772949219)"><foreignObject width="171.23333740234375" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;">Stage 1 - build 3d matrix</div></foreignObject></g></g></g><g class="node" style="opacity: 1;" id="B" transform="translate(129.38333129882812,312.3249740600586)"><rect rx="0" ry="0" x="-80.44999694824219" y="-23.35832977294922" width="160.89999389648438" height="46.71665954589844"></rect><g class="label" transform="translate(0,0)"><g transform="translate(-70.44999694824219,-13.358329772949219)"><foreignObject width="140.89999389648438" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;">Stage 2 - apply filter</div></foreignObject></g></g></g><g class="node" style="opacity: 1;" id="C" transform="translate(129.38333129882812,435.75829315185547)"><rect rx="0" ry="0" x="-109.38333129882812" y="-23.35832977294922" width="218.76666259765625" height="46.71665954589844"></rect><g class="label" transform="translate(0,0)"><g transform="translate(-99.38333129882812,-13.358329772949219)"><foreignObject width="198.76666259765625" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;">Stage 3 - create result image</div></foreignObject></g></g></g><g class="node" style="opacity: 1;" id="O" transform="translate(129.38333129882812,594.1332855224609)"><circle x="-58.30000305175781" y="-23.35832977294922" r="58.30000305175781"></circle><g class="label" transform="translate(0,0)"><g transform="translate(-48.30000305175781,-13.358329772949219)"><foreignObject width="96.60000610351562" height="26.716659545898438"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; white-space: nowrap;">Result Images</div></foreignObject></g></g></g></g></g></g></svg></div>
<p>Each stage in the pipeline can be performed in parallel for a given set of images. We provide an interface called `Operation’ that needs to be implemented to provide sequential portions of the code that are intended to be parallelized.</p>
<p>Following is a snippet for stage 1 of the edge detection program:</p>
<pre><code>public class ImageConvolutionStage1 implements Operation&lt;BufferedImage, double[][][]&gt; { 

	@Override
	public double[][][] execute(BufferedImage inputParam) throws Exception {		
		// This method contains the sequential code that is intended to run in parallel with other sequential code
		int width = inputParam.getWidth();
		int height = inputParam.getHeight();
		...	
</code></pre>
<p><br><br>
Once we have out sequential portions of code, we need to nest it in the <code>SequentialOpSkeleton</code> which is one of the three <code>Skeleton</code> implementations currently supported by the library.</p>
<pre><code>Skeleton stage1 = new SequentialOpSkeleton&lt;BufferedImage, double[][][]&gt;(o1, double[][][].class);
Skeleton stage2 = new SequentialOpSkeleton&lt;double[][][], double[][]&gt;(o2, double[][].class);
...
</code></pre>
<p><br><br>
Now that we have three sequential skeletons ready, we can create our final <code>PipelineSkeleton</code> which represents the outermost skeleton nesting to which we supply the input (list of images)</p>
<pre><code>Skeleton[] stages = { stage1, stage2, stage3 };
Skeleton pipeLine = new PipelineSkeleton(stages, ArrayList.class); // The outermost skeleton nesting
Future&lt;List&lt;File&gt;&gt; outputFuture = pipeLine.submitData(imageList); // Submit input data to the pipeline skeleton
</code></pre>
<p><br> The result of the parallel computation is represented by a <code>Future</code> object.</p>
<pre><code>Future&lt;List&lt;File&gt;&gt; outputFuture = pipeLine.submitData(imageList); // Future object containing the result of the parallel computation
</code></pre>
<h2 id="examples">Examples</h2>
<h3 id="synthetic-benchmarks">Synthetic Benchmarks</h3>
<p>A few synthetic benchmarks for the shared memory skeletons are included. These can executed by the following:<br>
Navigate to the root of the project folder (one containing the pom.xml file)<br>
Compile source code:</p>
<pre><code>mvn compile
</code></pre>
<p>The different benchmarks can be run via the following commands:</p>
<ul>
<li>
<p><u>Three stage pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed</p>
<pre><code> java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ThreeStagePipeline [numberOfCores][inputSize]
</code></pre>
</li>
<li>
<p><u>Task farm:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed<br>
<em>[chunkSize]</em> with the number of tasks per worker<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm</p>
<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.SimpleTaskFarm [numberOfCores][inputSize][chunkSize][numberOfWorkers]
</code></pre>
</li>
<li>
<p><u>Nesting pipeline in farm:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm</p>
<pre><code> java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.NestingPipelineInFarm [numberOfCores][inputSize][numberOfWorkers]
</code></pre>
</li>
<li>
<p><u>Nesting farm in pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm</p>
<pre><code> java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.NestingFarmInPipeline [numberOfCores][inputSize][numberOfWorkers]
</code></pre>
</li>
</ul>
<h3 id="edge-detection">Edge Detection</h3>
<p>A simple edge detection program is also included in the library as an example. Images can be placed in the<br>
<code>examples/src/main/resources/inputImages/test</code> folder. After executing the program, the resultant images will be stored in the <code>examples/src/main/resources/outputImages</code> folder.</p>
<p>The following commands can be used to run the program in different skeleton configurations:</p>
<ul>
<li><u>Pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the program with<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ImageConvolutionPipeline [numberOfCores] 3
</code></pre>
</li>
<li><u>Farm in pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the program with<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" 	uk.ac.standrews.cs.mpp.examples.skeletons.ImgConvolNestingFarmInPipeline [numberOfCores] 3 [numberOfWorkers]
</code></pre>
</li>
<li><u>Pipeline in farm:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the program, with<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ImgConvolNestingPipelineInFarm [numberOfCores] 3 [numberOfWorkers]
</code></pre>
</li>
</ul>
<p>The performance of these parallel versions can be measured up against a sequential version of the same program which can be run by the following command</p>
<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ImageConvolutionSequential 3
</code></pre>
<h4 id="running-the-distributed-version">Running the distributed version</h4>
<p><em>MPJExpress</em> needs to be installed and configured in order to run the distributed version of the synthetic benchmark and edge detection application.  They can be run in the multi-core mode of <em>MPJExpress</em> through the following commands</p>
<ul>
<li>
<p><u>Edge Detection \w Pipeline Skeleton</u><br>
replace:<br>
<em>[numberOfSystems]</em> with the number of nodes in the distributed system (in the multi-core mode of <em>MPJExpress</em>, this refers to the number of cores)<br>
<em>[numberOfCores]</em> with the number of processors at the shared memory level</p>
<pre><code>mpjrun -np [numberOfSystems] -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.mpj.MPPDistImgConvolution [numberOfCores] 3
</code></pre>
</li>
<li>
<p><u>Edge Detection \w Nested Skeletons</u><br>
replace:<br>
<em>[numberOfSystems]</em> with the number of nodes in the 		distributed system (in the multi-core mode of <em>MPJExpress</em>, this refers to the number of cores)<br>
<em>[numberOfCores]</em> with the number of processors at the shared memory level	<br>
[numberOfWorkers] with the total number of workers in the farm</p>
<pre><code>mpjrun -np [numberOfSystems] -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.mpj.MPPDistImgConvolutionPipeInFarm [numberOfCores] 3 [numberOfWorkers]
</code></pre>
</li>
</ul>
<h3 id="sample-outputs">Sample Outputs</h3>
<p>Following is a sample output of the image convolution application:</p>
<p><em><strong>WIP - Sample outputs to be added soon!</strong></em></p>
<h2 id="performance">Performance</h2>
<p>Following are performance figures of the benchmarks and the edge detection application when executed on a research server having two Intel® Xeon® CPUs clocked at 2.60 Ghz with 14 cores each, for a total of 28 cores.</p>
<h3 id="edge-detection-program">Edge detection program</h3>
<p>The maximum speedup for the distributed edge detection program for a total of 488 images (resolutions ranging between 1080p and 600p) is <em><strong>6.7</strong></em>.<br>
<strong>The sequential version runs in 97</strong> seconds while the parallel version, using the <em>Pipe(Seq, Farm(Seq), Seq)</em> skeleton<br>
composition with 8 nodes and 4 farm workers per node, <strong>runs in 19.3 seconds.</strong></p>
<p><em><strong>WIP - Supporting graphs and figures to be added soon!</strong></em></p>
<blockquote>
<p>Written with <a href="https://stackedit.io/">StackEdit</a>.</p>
</blockquote>

