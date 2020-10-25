// EVMC: Ethereum Client-VM Connector API.
// Copyright 2019-2020 The EVMC Authors.
// Licensed under the Apache License, Version 2.0.
package org.ethereum.evmc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.ethereum.evmc.types.Address;
import org.ethereum.evmc.types.Bytes32;

/**
 * The Host interface.
 *
 * <p>The set of all callback functions expected by VM instances.
 */
final class Host {
  private static ByteBuffer ensureDirectBuffer(ByteBuffer input) {
    // Reallocate if needed.
    if (!input.isDirect()) return ByteBuffer.allocateDirect(input.remaining()).put(input);
    return input;
  }

  /** Check account existence callback function. */
  static boolean account_exists(HostContext context, byte[] address) {
    return context.accountExists(new Address(address));
  }

  /** Get storage callback function. */
  static ByteBuffer get_storage(HostContext context, byte[] address, byte[] key) {
    return context.getStorage(new Address(address), new Bytes32(key)).getByteBuffer();
  }

  /** Set storage callback function. */
  static int set_storage(HostContext context, byte[] address, byte[] key, byte[] value) {
    StorageStatus status =
        context.setStorage(new Address(address), new Bytes32(key), new Bytes32(value));
    return status.code;
  }

  /** Get balance callback function. */
  static ByteBuffer get_balance(HostContext context, byte[] address) {
    return context.getBalance(new Address(address)).getByteBuffer();
  }

  /** Get code size callback function. */
  static int get_code_size(HostContext context, byte[] address) {
    return context.getCodeSize(new Address(address));
  }

  /** Get code hash callback function. */
  static ByteBuffer get_code_hash(HostContext context, byte[] address) {
    return context.getCodeHash(new Address(address)).getByteBuffer();
  }

  /** Copy code callback function. */
  static ByteBuffer copy_code(HostContext context, byte[] address) {
    return ensureDirectBuffer(context.getCode(new Address(address)));
  }

  /** Selfdestruct callback function. */
  static void selfdestruct(HostContext context, byte[] address, byte[] beneficiary) {
    context.selfdestruct(new Address(address), new Address(beneficiary));
  }

  /** Call callback function. */
  static ByteBuffer call(HostContext context, ByteBuffer msg) {
    return ensureDirectBuffer(context.call(msg));
  }

  /** Get transaction context callback function. */
  static ByteBuffer get_tx_context(HostContext context) {
    return context.getTxContext().getByteBuffer();
  }

  /** Get block hash callback function. */
  static ByteBuffer get_block_hash_fn(HostContext context, long number) {
    return context.getBlockHash(number).getByteBuffer();
  }

  /** Emit log callback function. */
  static void emit_log(
      HostContext context,
      byte[] address,
      byte[] data,
      int data_size,
      byte[][] topics,
      int topic_count) {
    context.emitLog(
        new Address(address),
        data,
        Arrays.stream(topics).map(Bytes32::new).toArray(Bytes32[]::new));
  }
}
