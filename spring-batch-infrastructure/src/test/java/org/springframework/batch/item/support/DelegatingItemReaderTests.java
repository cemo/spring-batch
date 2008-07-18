/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.item.support;

import junit.framework.TestCase;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

/**
 * Unit test for {@link DelegatingItemReader}
 * 
 * @author Robert Kasanicky
 */
public class DelegatingItemReaderTests extends TestCase {

	// object under test
	private DelegatingItemReader<Object> itemReader = new DelegatingItemReader<Object>();

	private ItemReader<Object> delegateReader;
	
	private ExecutionContext executionContext;

	// create input template and inject it to data provider
	protected void setUp() throws Exception {
		executionContext = new ExecutionContext();
		delegateReader = new MockItemReader(this, executionContext);
		itemReader.setItemReader(delegateReader);
	}

	public void testAfterPropertiesSet() throws Exception {
		// shouldn't throw an exception since the input source is set
		itemReader.afterPropertiesSet();
	}

	public void testNullItemReader() {
		try {
			itemReader.setItemReader(null);
			itemReader.afterPropertiesSet();
			fail();
		}
		catch (Exception ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	/**
	 * Uses input template to provide the domain object.
	 * @throws Exception
	 */
	public void testNext() throws Exception {
		Object result = itemReader.read();
		assertSame("domain object is provided by the input template", this, result);
	}

	private static class MockItemReader extends AbstractItemStreamItemReader<Object> {

		private Object value;

		public void update(ExecutionContext executionContext) {
			executionContext.putString("value", "foo");
		}

		public MockItemReader(Object value, ExecutionContext executionContext) {
			this.value = value;
		}

		public Object read() {
			return value;
		}

		public void close(ExecutionContext executionContext) {
		}

		public void open(ExecutionContext executionContext) {
		}

		public void mark() {
		}

		public void reset() {
		}

	}

}
